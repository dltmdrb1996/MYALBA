package com.bottotop.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.util.DateTime
import com.bottotop.core.util.DateTimeUtil.Companion.getDateRangeByNumberOfDays
import com.bottotop.model.Schedule
import com.bottotop.model.ScheduleInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor() : BaseViewModel("일정뷰모델") {

    private val dataUtil = DateTime()
    private val _schedule = MutableLiveData<Map<Int ,List<Schedule>>>()
    val schedule: LiveData<Map<Int ,List<Schedule>>>
        get() = _schedule

    fun setViewPagerData(month : Int){
        var startOfNextWeek = 1
        val endDay = dataUtil.monthEnd(month)
        var result = mutableMapOf<Int ,List<Schedule>>()
        for(i in 0..4){
            lateinit var week : List<String?>
            if(i==4 && startOfNextWeek!=endDay){
                week = dataUtil.getDateArrayOfDaysBetweenDates(dataUtil.getWeekStartDate(startOfNextWeek , month),
                    dataUtil.getWeekEndDate(endDay,month))?.map { dataUtil.getDateTimeToString(it) }!!
            }else {
                week = dataUtil.getDateArrayOfDaysBetweenDates(
                    dataUtil.getWeekStartDate(startOfNextWeek, month),
                    dataUtil.getWeekEndDate(startOfNextWeek, month)
                )?.map { dataUtil.getDateTimeToString(it) }!!
            }
            val list : List<Schedule> = week.map {
                val cMonth = it?.take(2)?.toInt()
                val day  = it?.drop(3)
                Schedule( cMonth!! , month , day!! , listOf(
                ScheduleInfo("이승규","07","15"),
                ScheduleInfo("이승규","07","15"),
                ScheduleInfo("이승규","07","15"),
                ScheduleInfo("이승규","07","15"),
                ScheduleInfo("이승규","07","15"),
                ScheduleInfo("이승규","07","15"),
            )) }
            if(startOfNextWeek+7 > endDay){
                startOfNextWeek = endDay
            }else{
                startOfNextWeek = getDateRangeByNumberOfDays(dataUtil.getWeekStartDate(startOfNextWeek,month),7)
            }
            result.put(i , list)
        }
        _schedule.value = result
        handleLoading(false)
    }


}