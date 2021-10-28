package com.bottotop.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.ext.withDelayOnMain
import com.bottotop.core.global.SocialInfo
import com.bottotop.model.repository.DataRepository
import com.bottotop.model.repository.SocialLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bottotop.core.model.LoginState
import com.bottotop.model.APIError
import com.bottotop.model.User
import com.bottotop.model.wrapper.APIResult


@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val socialLoginRepository: SocialLoginRepository,
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("스플래쉬뷰모델") {

    private val context = context
    private lateinit var user: User

    private val _movie = MutableLiveData<String>()
    val movie: LiveData<String> = _movie

    private val _login = MutableLiveData<LoginState>()
    val login: LiveData<LoginState> = _login


    init {
        viewModelScope.launch(dispatcherProvider.io) {
            checkUserData()
        }
    }

    suspend fun checkUserData() {
        val userResult = dataRepository.refreshUser(SocialInfo.id)
        when (userResult) {
            is APIResult.Success -> {
                user = dataRepository.getUser(SocialInfo.id)
                Log.e(TAG, "checkUserData: ${user}", )
                checkSocialToken()
            }
            is APIResult.Error -> {
                when (userResult.error) {
                    is APIError.SeverError -> {
                        showToast("서버접속이 원할하지 않습니다.")
                    }
                    is APIError.KeyValueError -> _login.postValue(LoginState.NoData)
                    is APIError.NullValueError -> showToast("데이터가 없습니다.")
                    is APIError.Error -> {
                        Log.e(TAG, "getAPIError: ${(userResult.error as APIError.Error).e}")
                        showToast("에러가 발생했습니다.")
                    }
                }
            }
        }
    }

    private suspend fun checkSocialToken() {
        if(user.social == "naver"){
            checkNaver()
        }
        else if(user.social == "kakao"){
            checkKakao()
        }else{
            Log.e(TAG, "checkSocialToken: noToken", )
            _login.postValue(LoginState.NoToken)
        }
    }

    private suspend fun checkKakao() {
        socialLoginRepository.refreshKakao()
        if (socialLoginRepository.checkKakao()) {
            Log.e(TAG, "searchToken: 카카오접근")
            SocialInfo.social = "kakao"
            socialLoginRepository.getKakaoInfo()
            if (user.company == "null") {
                _login.postValue(LoginState.NoCompany)
            } else {
                val success = getAPIResult(dataRepository.refreshCompanies(user.company))
                if(success) _login.postValue(LoginState.Success)
                _login.postValue(LoginState.Success)
            }
        }else{
            _login.postValue(LoginState.NoToken)
        }
    }

    private suspend fun checkNaver() {
        socialLoginRepository.refreshNaver()
        if (socialLoginRepository.checkNaver()) {
            Log.e(TAG, "searchToken: 네이버접근")
            SocialInfo.social = "naver"
            socialLoginRepository.getNaverInfo()
            if (user.company == "null") {
                _login.postValue(LoginState.NoCompany)
            }
            else {
                val success = getAPIResult(dataRepository.refreshCompanies(user.company))
                if(success) _login.postValue(LoginState.Success)
                _login.postValue(LoginState.Success)
            }
        } else{
            _login.postValue(LoginState.NoToken)
        }
    }

}