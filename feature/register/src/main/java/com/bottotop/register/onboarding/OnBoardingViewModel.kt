package com.bottotop.register.onboarding

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.core.util.DateTime
import com.bottotop.model.query.SetUserQuery
import com.bottotop.model.repository.DataRepository
import com.bottotop.model.repository.SocialLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val socialLoginRepository: SocialLoginRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("온보딩모델") {

    init {
//        getUserInfo()
    }

    private val _success = MutableLiveData<Boolean>()
    val success : LiveData<Boolean> = _success

    private fun getUserInfo(){
        viewModelScope.launch(dispatcherProvider.io) {
            if(SocialInfo.social=="naver") socialLoginRepository.getNaverInfo()
            else socialLoginRepository.getKakaoInfo()
        }
    }

    fun setUser() {
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            val setUser = dataRepository.setUser(
                SetUserQuery(SocialInfo.id,SocialInfo.tel,SocialInfo.birth,SocialInfo.name,
                SocialInfo.email,SocialInfo.social,"null","off")
            ).result(Error().stackTrace)
            if(setUser) {
                val refreshUser = dataRepository.refreshUser(SocialInfo.id).result(Error().stackTrace)
                handleLoading(false)
                if(refreshUser) _success.postValue(true)
                else showToast("유저생성에 에러가 발생했습니다 잠시후 다시실행해주세요")
            }else{
                handleLoading(false)
                _success.postValue(false)
            }
        }
    }


}