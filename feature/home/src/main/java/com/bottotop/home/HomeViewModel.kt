package com.bottotop.home

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("홈뷰모델") {

    private val _info = MutableLiveData<Boolean>()
    val info : LiveData<Boolean> = _info

    init {
        if(UserInfo.name.isEmpty()) {
            loadUser()
        }else{
            handleLoading(false)
            _info.value= true
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun loadUser(){
        viewModelScope.launch(dispatcherProvider.io) {
            when(UserInfo.login_flag){
                LoginFlag.Kakao -> {
                    if(loginRepository.getKakaoInfo()){
                        _info.postValue(true)
                        handleLoading(false)
                    }else{
                        Log.e(TAG, "searchToken: 카카오 정보 불러오기 실패", )
                        handleLoading(false)
                    }
                }
                LoginFlag.Naver -> {
                    if(loginRepository.getNaverInfo()){
                        _info.postValue(true)
                        handleLoading(false)
                    }else{
                        Log.e(TAG, "searchToken: 네이버 정보 불러오기 실패", )
                        handleLoading(false)
                    }
                }
            }
        }
    }

    fun logoutKakao() {
        viewModelScope.launch(dispatcherProvider.io) {
            loginRepository.loagoutKakao()
        }
    }

    fun disconnectKakao() {
        viewModelScope.launch(dispatcherProvider.io) {
            loginRepository.disconectKakao()
        }
    }

    fun logoutNaver() {
        viewModelScope.launch(dispatcherProvider.io) {
            loginRepository.logoutNaver()
        }
    }

    fun disconnectNaver() {
        viewModelScope.launch(dispatcherProvider.io) {
            loginRepository.disconectNaver()
        }
    }


}