package com.bottotop.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.core.util.DateTime
import com.bottotop.model.*
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


    private val _schedule = MutableLiveData<Schedule>()
    val schedule: LiveData<Schedule> = _schedule

    private val _workTime = MutableLiveData<String>("")
    val workTime: LiveData<String> = _workTime

    private val _workPay = MutableLiveData<String>("")
    val workPay: LiveData<String> = _workPay

    private val _workOn = MutableLiveData<String>()
    val workOn: LiveData<String> = _workOn

    private val _scheduleItem = MutableLiveData<List<ScheduleItem>>()
    val scheduleItem = _scheduleItem

    private val _master = MutableLiveData<Boolean>()
    val master = _master

    private val today = DateTime().getTOdayWeek()
    private lateinit var user: User
    private lateinit var company: Company
    private lateinit var companise: List<Company>
    private lateinit var member: List<User>
    private val dataUtil = DateTime()

    init {
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            try{
                user = dataRepository.getUser(SocialInfo.id)
                company = dataRepository.getCompany(SocialInfo.id)
                companise = dataRepository.getCompanies()
                member = dataRepository.getMembers()
                if(company.position=="A") _master.postValue(true)
                getMemberWorkDay()
            }catch (e : Throwable){
                showToast("에러가발생했습니다.")
                Log.e(TAG, "홈뷰모델 room load: $e" )
            }

            if(user.workOn == "off") _workOn.postValue("출근하기")
            else {
                _workOn.postValue("퇴근하기")
                val daySchedule = dataRepository.getDaySchedule()
                patchSchedule(daySchedule)
            }
        }
        handleLoading(false)
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

    fun checkWork(){
        viewModelScope.launch(dispatcherProvider.io){
            if(workOn.value=="출근하기") startWork()
            else endWork()
        }
    }

    fun endWork() {
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            val daySchedule = dataRepository.getDaySchedule()
            val current = dataUtil.getCurrentDateTimeAsLong()
            val startTime = daySchedule.time
            val workTime = (dataUtil.getCurrentDateTimeAsLong().toLong() - startTime.toLong()).toString()

            val patchSchedule = getAPIResult(
                dataRepository.patchSchedule(
                    mapOf(
                        Pair("id", SocialInfo.id),
                        Pair("month", dataUtil.getYearMonth()),
                        Pair("day", daySchedule.day),
                        Pair("target", "endTime"),
                        Pair("change", current),
                        Pair("target2","workTime"),
                        Pair("change2",workTime)
                    )
                ), "$TAG : patchSchedule"
            )

            if (patchSchedule) {
                showTimePay(workTime)
                _workOn.postValue("출근하기")
                getAPIResult(
                    dataRepository.updateUser(
                        mapOf(
                            Pair("id", SocialInfo.id),
                            Pair("target", "workOn"),
                            Pair("change", "off")
                        )
                    ), "$TAG : updateUser"
                )
                dataRepository.deleteDaySchedule()
                getAPIResult(dataRepository.refreshUser(SocialInfo.id),"$TAG : refreshUser")

            }
            handleLoading(false)
        }
    }

    fun startWork() {
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            val current = dataUtil.getCurrentDateTimeAsLong()
            val updateSchedule = getAPIResult(
                dataRepository.updateSchedule(
                    mapOf(
                        Pair("startTime", current),
                        Pair("id", SocialInfo.id),
                        Pair("month", dataUtil.getYearMonth()),
                        Pair("day", dataUtil.getToday()),
                        Pair("workTime", (dataUtil.getCurrentDateTimeAsLong().toLong() - current.toLong()).toString()
                        )
                    )
                ), "$TAG : updateSchedule"
            )

            if (updateSchedule) {
                val updateUser = getAPIResult(
                    dataRepository.updateUser(
                        mapOf(
                            Pair("id", SocialInfo.id),
                            Pair("target", "workOn"),
                            Pair("change", "on")
                        )
                    ), "$TAG : updateUser"
                )

                if(updateUser){
                    dataRepository.insertDaySchedule(dataUtil.getToday(),current)
                    getAPIResult(dataRepository.refreshUser(SocialInfo.id),"$TAG : refreshUser")
                }
            }
            showTimePay("1000")
            handleLoading(false)
        }
    }

    private fun patchSchedule(daySchedule: DaySchedule ) {
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            val start = daySchedule.time.toLong()
            val current = dataUtil.getCurrentDateTimeAsLong().toLong()
            val workTime = (current - start).toString()
            showTimePay(workTime)
            handleLoading(false)
        }
    }

    private fun showTimePay(workTime : String){
        val timeString = dataUtil.getTimeLongToString(workTime.toLong())
        val time = workTime.toDouble()/1000
        val payOfSecond = (company.pay.toDouble()/60/60)
        val workPay = "${(time*payOfSecond).toInt()}원"
        _workPay.postValue(workPay)
        _workTime.postValue(timeString)
    }

    private fun getMemberWorkDay(){
        val day = when(today){
            "월요일" -> 0
            "화요일" -> 1
            "수요일" -> 2
            "목요일" -> 3
            "금요일" -> 4
            "토요일" -> 5
            else -> 6
        }
        val list = mutableListOf<ScheduleItem>()

        (member.indices).forEach {
            if(companise[it].workday[day]=='1') {
                val start = if(companise[it].start.length==1) "0${companise[it].start}" else companise[it].start
                val end = if(companise[it].end.length==1) "0${companise[it].end}" else companise[it].end
                list += ScheduleItem(member[it].name, emptyList(), start, end)
            }
        }
        _scheduleItem.postValue(list)
    }
}
