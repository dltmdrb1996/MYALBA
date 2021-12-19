package com.bottotop.schedule

import androidx.lifecycle.*
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.util.DateTime
import com.bottotop.model.*
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val dataRepository: DataRepository
) : BaseViewModel("일정뷰모델") {

    private val dataUtil = DateTime()

    private val _scheduleItem = MutableLiveData<List<ScheduleItem>>()
    val scheduleItem = _scheduleItem

    private val _viewPager = MutableLiveData<Map<Int, List<List<String>>>>()
    val viewPager : LiveData<Map<Int, List<List<String>>>> = _viewPager

    val month : MutableLiveData<Int> = MutableLiveData(dataUtil.currentMonth)
    val nextMonth : LiveData<String> = Transformations.map(month) { if(it<12) "${it+1}월" else " " }
    val currentMonth : LiveData<String> = Transformations.map(month) { "${it}월" }
    val preMonth : LiveData<String> = Transformations.map(month) { if(it>1) "${it-1}월" else " " }

    private lateinit var member : List<User>
    private lateinit var company : List<Company>
    private lateinit var schedules : List<Schedule>


    init {
        viewModelScope.launch(dispatcherProvider.io){
            handleLoading(true)
            try{
                company = dataRepository.getCompanies()
                member = dataRepository.getMembers()
                schedules = dataRepository.getScheduleAll(
                    mapOf(Pair("com_id", member[0].company))
                ).getOrNull()!!
                getMemberWorkDay()
                setViewPagerData(month.value!!)
            }catch (e : Throwable){
                showToast("데이터를 불러오는데 실패했습니다.")
                Timber.e(": 룸데이터 불러오기 $e")
            }
            handleLoading(false)
        }
    }

    // 뷰페이저에 넣어질 달력폼 날짜데이터가공
    fun setViewPagerData(month: Int) {
        var startOfNextWeek = 1
        val endDay = dataUtil.monthEnd(month)
        val result = mutableMapOf<Int, List<List<String>>>()
        for (i in 0..4) {
            lateinit var week: List<String>
            if (i == 4 && startOfNextWeek != endDay) {
                week = dataUtil.getDateArrayOfDaysBetweenDates(
                    dataUtil.getWeekStartDate(startOfNextWeek, month),
                    dataUtil.getWeekEndDate(endDay, month)
                ).map { dataUtil.getDateTimeToString(it)+" ${if(month<=9) "0$month" else month.toString()}"  }
            } else {
                week = dataUtil.getDateArrayOfDaysBetweenDates(
                    dataUtil.getWeekStartDate(startOfNextWeek, month),
                    dataUtil.getWeekEndDate(startOfNextWeek, month)
                ).map { dataUtil.getDateTimeToString(it)+" ${if(month<=9) "0$month" else month.toString()}" }
            }
            if (startOfNextWeek + 7 > endDay) startOfNextWeek = endDay
            else {
                startOfNextWeek = dataUtil.getDateRangeByNumberOfDays(
                    dataUtil.getWeekStartDate(startOfNextWeek, month), 7
                )
            }
            val list = week.map { it.split(" ") }
            result[i] = list
        }
        _viewPager.postValue(result)
    }

    private fun getMemberWorkDay(){
        val scheduleItem : List<ScheduleItem> = (member.indices).map { i ->
            val list = mutableListOf<String>()
            company[i].workday.mapIndexed { index, c ->
                if(c=='1'){
                    when(index){
                        0 -> list.add("월요일")
                        1 -> list.add("화요일")
                        2 -> list.add("수요일")
                        3 -> list.add("목요일")
                        4 -> list.add("금요일")
                        5 -> list.add("토요일")
                        6 -> list.add("일요일")
                    }
                }
            }
            ScheduleItem(member[i].name,list,company[i].start,company[i].end)
        }
        _scheduleItem.postValue(scheduleItem)
    }

    fun nextMonth(month : Int){
        if(month<12) {
            setViewPagerData(month+1)
            this.month.value = month+1
        }
    }

    fun preMonth(month: Int){
        if(month>1) {
            setViewPagerData(month-1)
            this.month.value = month-1
        }
    }

}