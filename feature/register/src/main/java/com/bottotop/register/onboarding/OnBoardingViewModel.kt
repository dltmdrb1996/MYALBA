package com.bottotop.register.onboarding

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.model.repository.DataRepository
import com.bottotop.model.repository.SocialLoginRepository
import com.bottotop.model.wrapper.APIResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val socialLoginRepository: SocialLoginRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("온보딩모델") {

    init {
        getUserInfo()
    }

    private val _success = MutableLiveData<Boolean>()
    val success : LiveData<Boolean> = _success

    fun getUserInfo(){
        viewModelScope.launch(dispatcherProvider.io) {
            if(SocialInfo.social=="naver"){
                socialLoginRepository.getNaverInfo()
            }else{
                socialLoginRepository.getKakaoInfo()
            }
        }
    }

    fun setUser() {
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            val success = getAPIResult(dataRepository.setUser(
                mapOf<String, String>(
                    // 여기서 id를 이메일로 바꾸면됨 네이버로그인이 계속 유지되면 바꾸는 시도 ㄱㄱ
                    Pair("id", SocialInfo.id),
                    Pair("tel", SocialInfo.tel),
                    Pair("birth", SocialInfo.birth),
                    Pair("name", SocialInfo.name),
                    Pair("email", SocialInfo.email),
                    Pair("social", SocialInfo.social),
                    Pair("com_id", "null")
                )
            ))
            Log.e(TAG, "setUser: ${success}", )
            if(success) {
                dataRepository.refreshUser(SocialInfo.id)
                handleLoading(false)
                _success.postValue(true)
                Log.e(TAG, "setUser: ${dataRepository.getUser(SocialInfo.id)}", )
            }else{
                handleLoading(false)
                _success.postValue(false)
            }
        }
    }


}