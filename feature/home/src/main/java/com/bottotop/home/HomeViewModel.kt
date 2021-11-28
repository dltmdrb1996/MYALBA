package com.bottotop.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.core.util.DateTime
import com.bottotop.model.*
import com.bottotop.model.query.PatchScheduleQuery
import com.bottotop.model.query.UpdateScheduleQuery
import com.bottotop.model.query.UpdateUserQuery
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val dataRepository: DataRepository
) : BaseViewModel("홈뷰모델") {


    private val _workTime = MutableLiveData("")
    val workTime: LiveData<String> = _workTime

    private val _workPay = MutableLiveData("")
    val workPay: LiveData<String> = _workPay

    private val _workOn = MutableLiveData<String>()
    val workOn: LiveData<String> = _workOn

    private val _scheduleItem = MutableLiveData<List<ScheduleItem>>()
    val scheduleItem : LiveData<List<ScheduleItem>> = _scheduleItem

    private val _master = MutableLiveData<Boolean>()
    val master : LiveData<Boolean> = _master

    private val _community = MutableLiveData<Community>()
    val community : LiveData<Community> = _community

    private val _working = MutableLiveData<String>()
    val working: LiveData<String> = _working

    private val today = DateTime().getTOdayWeek()
    private lateinit var user: User
    private lateinit var company: Company
    private lateinit var companise: List<Company>
    private lateinit var member: List<User>
    private val dataUtil = DateTime()

    init {
        Timber.e("test ${SocialInfo.id}  ,  ${SocialInfo.social}")
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            handleLoading(true)
            try{
                if(initData()){
                    initFirst()
                    initWorking()
                    getMemberWorkDay()
                }
            }catch (e : Throwable){
                showToast("에러가발생했습니다.")
                Timber.e("홈뷰모델 init: $e")
            }
            handleLoading(false)
        }
    }

   private suspend fun initFirst(){
       val getCommunity = dataRepository.getCommunity(user.company)

       if(getCommunity.isSuccess && getCommunity.getOrNull()?.isNotEmpty()!!) {
           _community.postValue(getCommunity.getOrNull()!!.last())
       }
       if(company.position=="A") _master.postValue(true)

       if(user.workOn == "off") {
           _workOn.postValue("출근하기")
       } else {
           _workOn.postValue("퇴근하기")
           val daySchedule = dataRepository.getDaySchedule()
           patchSchedule(daySchedule)
       }
    }

    fun checkWork(){
        viewModelScope.launch(dispatcherProvider.io){
            if(workOn.value=="출근하기") startWork()
            else endWork()
        }
    }

    private fun endWork() {
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            val daySchedule = dataRepository.getDaySchedule()
            val current = dataUtil.getCurrentDateTimeAsLong()
            val startTime = daySchedule.time
            val workTime = (dataUtil.getCurrentDateTimeAsLong().toLong() - startTime.toLong()).toString()
            val time = workTime.toDouble()/1000
            val payOfSecond = (company.pay.toDouble()/60/60)
            val workPay = "${(time*payOfSecond).toInt()}"


            val patchSchedule = dataRepository.patchSchedule(
                    PatchScheduleQuery(SocialInfo.id, dataUtil.getYearMonth(),daySchedule.day,"endTime", current,"workTime",workTime,"workPay",workPay)
                ).result(Error().stackTrace)

            if (patchSchedule) {
                showTimePay(workTime)
                _workOn.postValue("출근하기")
                dataRepository.updateUser(UpdateUserQuery(SocialInfo.id , "workOn" , "off")).result(Error().stackTrace)
                dataRepository.deleteDaySchedule()
                dataRepository.refreshUser(SocialInfo.id).result(Error().stackTrace)
                _workOn.postValue("출근하기")
                initData()
                initWorking()
                _workPay.postValue("")
                _workTime.postValue("")

            }
            handleLoading(false)
        }
    }

    private fun startWork() {
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            val current = dataUtil.getCurrentDateTimeAsLong()

            val updateSchedule = dataRepository.updateSchedule(
                    UpdateScheduleQuery(current, SocialInfo.id, dataUtil.getYearMonth(), dataUtil.getToday(), (dataUtil.getCurrentDateTimeAsLong().toLong() - current.toLong()).toString())
                ).result(Error().stackTrace)

            if (updateSchedule) {
                val updateUser =
                    dataRepository.updateUser(
                        UpdateUserQuery(SocialInfo.id,"workOn","on")
                    ).result(Error().stackTrace)

                if(updateUser){
                    dataRepository.insertDaySchedule(dataUtil.getToday(),current)
                    dataRepository.refreshUser(SocialInfo.id).result(Error().stackTrace)
                }
            }

            _workOn.postValue("퇴근하기")
            if(initData()){
                initWorking()
                showTimePay("1000")
            }
            handleLoading(false)
        }
    }


    private suspend fun initData() : Boolean {
        return try {
            user = dataRepository.getUser(SocialInfo.id)
            company = dataRepository.getCompany(SocialInfo.id)
            companise = dataRepository.getCompanies()
            member = dataRepository.getMembers()
            true
        } catch (e : Throwable){
            Timber.e("initData: $e")
            false
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

    private fun initWorking(){
        var working = ""
        member.filter { it.workOn=="on" }.forEach { working+="${it.name}\n" }
        _working.postValue(working)
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
