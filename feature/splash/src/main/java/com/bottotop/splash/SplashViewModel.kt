package com.bottotop.splash

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.model.LoginFlag
import com.bottotop.model.UserInfo
import com.bottotop.model.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val loginRepository: LoginRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("스플래쉬뷰모델") {


    val aplication = Application()
    private val context = context

    private val _movie = MutableLiveData<String>()
    val movie: LiveData<String> = _movie

    private val _token = MutableLiveData<Boolean>()
    val token : LiveData<Boolean> = _token

    init {
        searchToken()
    }

    fun searchToken() {
        viewModelScope.launch(dispatcherProvider.io) {
            if(loginRepository.checkKakao()){
                Log.e(TAG, "searchToken: 카카오접근", )
                UserInfo.login_flag = LoginFlag.Kakao
                loginRepository.getKakaoInfo()
                _token.postValue(true)
            }else if(loginRepository.checkNaver()){
                Log.e(TAG, "searchToken: 네이버접근", )
                UserInfo.login_flag = LoginFlag.Naver
                loginRepository.getNaverInfo()
                _token.postValue(true)
            }else{
                _token.postValue(false)
            }
        }
    }

}