package com.bottotop.splash

import android.app.Application
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
import com.bottotop.model.User


@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val socialLoginRepository: SocialLoginRepository,
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel("스플래쉬뷰모델") {


    val aplication = Application()
    private val context = context
    private lateinit var user: User

    private val _movie = MutableLiveData<String>()
    val movie: LiveData<String> = _movie

    private val _login = MutableLiveData<LoginState>()
    val login: LiveData<LoginState> = _login


    init {
        viewModelScope.launch(dispatcherProvider.io) {
            Log.e(TAG, "${SocialInfo.id} , ?? ")
            dataRepository.refreshUser(SocialInfo.id)

//            Log.e(TAG, ": ${dataRepository.getUser()}")
//            user = dataRepository.getUser()
            check_Token_User()
        }
    }

    suspend fun check_Token_User() {
        // 카카오
        if (socialLoginRepository.checkKakao()) {
            Log.e(TAG, "searchToken: 카카오접근")
            socialLoginRepository.refreshKakao()
            SocialInfo.social = "kakao"
            socialLoginRepository.getKakaoInfo()
            if (user.code == "200") {
                if (user.company.isEmpty()) {
                    _login.postValue(LoginState.NoCompany)
                } else{
                    _login.postValue(LoginState.Suceess)
                }
            } else if (user.code == "404") {
                _login.postValue(LoginState.Register)
            }
        }
        // 네이버
        else if (socialLoginRepository.checkNaver()) {
            Log.e(TAG, "searchToken: 네이버접근")
            socialLoginRepository.refreshNaver()
            SocialInfo.social = "naver"
            socialLoginRepository.getNaverInfo()
            if (user.code == "200") {
                if (user.company == "null") {
                    _login.postValue(LoginState.NoCompany)
                } else{
                    _login.postValue(LoginState.Suceess)
                }
            } else if (user.code == "404") {
                _login.postValue(LoginState.Register)
            }
        } else {
            _login.postValue(LoginState.NoToken)
        }

    }

}