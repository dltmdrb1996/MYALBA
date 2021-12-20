package com.bottotop.setting

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.PreferenceHelper
import com.bottotop.core.global.PreferenceHelper.clear
import com.bottotop.core.global.PreferenceHelper.get
import com.bottotop.core.global.PreferenceHelper.set
import com.bottotop.core.global.SocialInfo
import com.bottotop.model.Company
import com.bottotop.model.Notification
import com.bottotop.model.User
import com.bottotop.model.repository.DataRepository
import com.bottotop.model.repository.SocialLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider,
    @ApplicationContext context: Context,
    ) : BaseViewModel("알림뷰모델") {

    private val mPref = PreferenceHelper.defaultPrefs(context)
    private val id: String? = mPref["id"]

    private val _noti = MutableLiveData<List<Notification>>()
    val noti : LiveData<List<Notification>> = _noti

    init {
        loadNotification()
        mPref.set("badge", 0)
    }

    private fun loadNotification(){
        viewModelScope.launch(dispatcherProvider.io){
            val result = dataRepository.getNotification()
            if(result.isSuccess){
                if(result.getOrNull()!=null){
                    _noti.postValue(result.getOrNull())
                } else {
                    Timber.e("데이터없음")
                }
            }else{
                Timber.e("Notification Room 불러오기 에러")
            }
        }
    }




}