package com.bottotop.setting

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
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


    lateinit var company : Company

    init {
        viewModelScope.launch(dispatcherProvider.io){
            company = dataRepository.getCompany(SocialInfo.id)
        }
    }

    fun fcmOn(){
        Timber.e("fmcOn")
        Firebase.messaging.subscribeToTopic(company.com_id)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.e("success")
                }
            }
    }

    fun fcmOff(){
        Timber.e("fmcOff")
        FirebaseMessaging.getInstance().unsubscribeFromTopic(company.com_id);
    }

}