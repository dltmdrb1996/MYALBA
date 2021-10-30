package com.bottotop.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.core.util.DateTime
import com.bottotop.model.Schedule
import com.bottotop.model.ScheduleInfo
import com.bottotop.model.User
import com.bottotop.model.repository.DataRepository
import com.bottotop.model.repository.SocialLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val socialLoginRepository: SocialLoginRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val dataRepository: DataRepository
) : BaseViewModel("홈뷰모델") {

    private val _info = MutableLiveData<Boolean>()
    val info : LiveData<Boolean> = _info

    private lateinit var user : User
    private val dataUtil = DateTime()
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            user = dataRepository.getUser(SocialInfo.id)
            val scheduleInfo = listOf<ScheduleInfo>(
                ScheduleInfo(
                    dataUtil.getToday(),
                    dataUtil.getCurrentDateTimeAsLong(),
                    dataUtil.getTimeLongToString(dataUtil.getCurrentDateTimeAsLong().toLong()),
                    "830"
                )
            )
            val schedule = Schedule(
                user.id,
                dataUtil.currentMonth.toString(),
                scheduleInfo
                )
            dataRepository.setSchedule(schedule)
            Log.e(TAG, "${dataRepository.getMembers()}: ")
            Log.e(TAG, "${dataRepository.getCompanies()}: ")
        }
    }

    override fun onCleared() {
        super.onCleared()
    }


    fun logoutKakao() {
        viewModelScope.launch(dispatcherProvider.io) {
            socialLoginRepository.loagoutKakao()
        }
    }

    fun disconnectKakao() {
        viewModelScope.launch(dispatcherProvider.io) {
            socialLoginRepository.disconectKakao()
        }
    }

    fun logoutNaver() {
        viewModelScope.launch(dispatcherProvider.io) {
            socialLoginRepository.logoutNaver()
        }
    }

    fun disconnectNaver() {
        viewModelScope.launch(dispatcherProvider.io) {
            socialLoginRepository.disconectNaver()
        }
    }


}