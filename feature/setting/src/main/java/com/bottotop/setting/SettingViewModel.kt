package com.bottotop.setting

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.PreferenceHelper
import com.bottotop.core.global.PreferenceHelper.get
import com.bottotop.core.global.PreferenceHelper.set
import com.bottotop.core.global.SocialInfo
import com.bottotop.model.Company
import com.bottotop.model.repository.DataRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider,
    @ApplicationContext context: Context,
) : BaseViewModel("셋팅뷰모델") {
    private val mPref = PreferenceHelper.defaultPrefs(context)
    lateinit var company : Company

    init {
        viewModelScope.launch(dispatcherProvider.io){
            company = dataRepository.getCompany(SocialInfo.id)
        }
    }

    fun clickFcm(){
        val fcm = mPref["fcm", true]
        Timber.e("$fcm")
        if(fcm) fcmOff()
        else fcmOn()
    }

    fun fcmOn(){
        Firebase.messaging.subscribeToTopic(company.com_id)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.e("success")
                }
            }
        mPref["fcm"] = true
    }

    fun fcmOff(){
        Timber.e("해제")
        FirebaseMessaging.getInstance().unsubscribeFromTopic(company.com_id)
        mPref["fcm"] = false
    }

}