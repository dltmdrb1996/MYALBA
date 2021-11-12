package com.bottotop.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.model.repository.DataRepository
import com.bottotop.model.repository.SocialLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bottotop.core.model.LoginState
import com.bottotop.core.util.DateTime
import com.bottotop.model.wrapper.APIError
import com.bottotop.model.User
import com.bottotop.model.query.SetScheduleQuery
import com.bottotop.model.wrapper.APIResult


@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val socialLoginRepository: SocialLoginRepository,
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("스플래쉬뷰모델") {

    private lateinit var user: User

    private val _login = MutableLiveData<LoginState>()
    val login: LiveData<LoginState> = _login

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            checkUserData()
        }
    }

    private suspend fun checkUserData() {
        val refreshUser = dataRepository.refreshUser(SocialInfo.id)
        when (refreshUser) {
            is APIResult.Success -> {
                user = dataRepository.getUser(SocialInfo.id)
                checkSocialToken()
            }
            is APIResult.Error -> {
                when (refreshUser.error) {
                    is APIError.SeverError -> {
                        Log.e(TAG, "checkUserData: SeverError" )
                        showToast("서버접속이 원할하지 않습니다 잠시 후 다시접속해 주세요.")
                    }
                    is APIError.KeyValueError -> {
                        Log.e(TAG, "checkUserData: KeyValueError" )
                        _login.postValue(LoginState.NoData)
                    }
                    is APIError.NullValueError -> {
                        Log.e(TAG, "checkUserData: NullValueError" )
                        showToast("데이터에 오류가 발생했습니다. 문의헤주세요")
                    }
                    is APIError.Error -> {
                        Log.e(TAG, "getAPIError: ${(refreshUser.error as APIError.Error).e}")
                        showToast("에러가 발생했습니다.")
                    }
                }
            }
        }
    }

    private suspend fun checkSocialToken() {
        when (user.social) {
            "naver" -> checkNaver()
            "kakao" -> checkKakao()
            else -> _login.postValue(LoginState.NoToken)
        }
    }

    private suspend fun checkNaver() {
        socialLoginRepository.refreshNaver()
        if (socialLoginRepository.checkNaver()) {
            SocialInfo.social = "naver"
            socialLoginRepository.getNaverInfo()
            if (user.company == "null") _login.postValue(LoginState.NoCompany)
            else {
                val refreshCompanies = getAPIResult(dataRepository.refreshCompanies(user.company), "$TAG : refreshCompanies")
                dataRepository.setSchedule(SetScheduleQuery(SocialInfo.id , DateTime().getYearMonth() , user.company))
                if(refreshCompanies) _login.postValue(LoginState.Success)
            }
        } else{
            if (user.company != "null") {
                getAPIResult(dataRepository.refreshCompanies(user.company), "$TAG : refreshCompanies")
                _login.postValue(LoginState.NoToken)
            }
            else _login.postValue(LoginState.NoToken)
        }
    }


    private suspend fun checkKakao() {
        socialLoginRepository.refreshKakao()
        if (socialLoginRepository.checkKakao()) {
            SocialInfo.social = "kakao"
            socialLoginRepository.getKakaoInfo()
            if (user.company == "null") {
                _login.postValue(LoginState.NoCompany)
            } else {
                val refreshCompanies = getAPIResult(dataRepository.refreshCompanies(user.company), "$TAG : refreshCompanies")
                if(refreshCompanies) {
                    _login.postValue(LoginState.Success)
                }
            }
        }else{
            if (user.company != "null") {
                getAPIResult(dataRepository.refreshCompanies(user.company), "$TAG : refreshCompanies")
                _login.postValue(LoginState.NoToken)
            }else{
                _login.postValue(LoginState.NoToken)
            }
        }
    }
}