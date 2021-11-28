package com.bottotop.splash

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.PreferenceHelper
import com.bottotop.core.global.PreferenceHelper.get
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
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel("스플래쉬뷰모델") {

    private lateinit var user: User

    private val _login = MutableLiveData<LoginState>()
    val login: LiveData<LoginState> = _login

    private val mPref = PreferenceHelper.defaultPrefs(context)
    private val id: String? = mPref["id"]
    private val social: String? = mPref["social"]
    private val timeUtil = DateTime()

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            checkUserData()
        }
    }

    private suspend fun checkUserData() {
        if (id.isNullOrEmpty()) {
            _login.postValue(LoginState.NoData)
        } else {
            val refreshUser = dataRepository.refreshUser(id)
            when (refreshUser) {
                APIResult.Success -> {
                    user = dataRepository.getUser(id)
                    checkSocialToken(social)
                }
                is APIResult.Error -> {
                    if(refreshUser.error == APIError.KeyValueError){
                        if(socialLoginRepository.checkNaver()) {
                            if(socialLoginRepository.getNaverInfo()) _login.postValue(LoginState.Register)
                            else showToast("네이버 정보를 불러오는 데 실패했습니다.")
                        }
                        else _login.postValue(LoginState.NoToken)
                    } else {
                        showToast("에러가 발생하였습니다 잠시후 다시시도해주세요")
                    }
                }
            }
        }
    }

    private suspend fun checkSocialToken(socialFlag: String?) {
        when (socialFlag) {
            "naver" -> checkNaver()
            "kakao" -> checkKakao()
            else -> _login.postValue(LoginState.NoToken)
        }
    }

    private suspend fun checkNaver() {
        socialLoginRepository.refreshNaver()
        if (socialLoginRepository.checkNaver()) {
            socialLoginRepository.getNaverInfo()
            if (user.company == "null") _login.postValue(LoginState.NoCompany)
            else {
                val refreshCompanies = dataRepository.refreshCompanies(user.company).result(Error().stackTrace)
                dataRepository.setSchedule(SetScheduleQuery(id!!, timeUtil.getYearMonth(), user.company))
                if (refreshCompanies) _login.postValue(LoginState.Success)
            }
        } else {
            _login.postValue(LoginState.NoToken)
        }
    }


    private suspend fun checkKakao() {
        socialLoginRepository.refreshKakao()
        if (socialLoginRepository.checkKakao()) {
            socialLoginRepository.getKakaoInfo()
            if (user.company == "null") _login.postValue(LoginState.NoCompany)
            else {
                val refreshCompanies = dataRepository.refreshCompanies(user.company).result(Error().stackTrace)
                dataRepository.setSchedule(
                    SetScheduleQuery(
                        id!!,
                        timeUtil.getYearMonth(),
                        user.company
                    )
                )
                if (refreshCompanies) _login.postValue(LoginState.Success)
            }
        } else {
            _login.postValue(LoginState.NoToken)
        }
    }
}