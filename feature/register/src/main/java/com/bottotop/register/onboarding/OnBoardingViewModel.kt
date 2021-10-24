package com.bottotop.register.onboarding

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("온보딩모델") {

    init {
        Log.e(TAG, "뷰모델시작")
    }

    fun clickTest(){
        Log.e(TAG, "clickTest: 됨?", )
    }

    fun test(){
        showToast("test")
    }

    fun setUser() {
        viewModelScope.launch(dispatcherProvider.io) {
            dataRepository.setUser(
                mapOf<String, String>(
                    Pair("id", SocialInfo.id),
                    Pair("tel", SocialInfo.tel),
                    Pair("birth", SocialInfo.birth),
                    Pair("name", SocialInfo.name),
                    Pair("email", SocialInfo.email),
                    Pair("com_id", "null")
                )
            )
        }
    }


}