/*
     *
     * BH Apps
     * version 0.0.2
     * Methods for Date/Time
     * bhapps.utilitools.kotlin.datetime
     *
*/

//todo: add java8 and joda time based functions

package com.bottotop.core.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*
import java.util.concurrent.TimeUnit

class DateTime {

    var yyyyMMddFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
    var yyyyMMddHHmmssFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.KOREA)
    var dayStartFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd 00:00:00", Locale.KOREA)
    val hhmmFormat: DateFormat = SimpleDateFormat("HH:mm:ss")
    var ddFormat: DateFormat = SimpleDateFormat("dd", Locale.KOREA)
    var wFormat: DateFormat = SimpleDateFormat("E요일", Locale.KOREA)
    var MMddEFormat : DateFormat = SimpleDateFormat("MM dd E요일", Locale.KOREA)

    private val yyyyMMFormat: String = "YYYYMM"
    private val TIME_PATTERN : String = "HH:mm"
    private val PATTERN : String = "MM dd일 E요일"
    private val DAY_PATTERN : String = "dd"

    //region timeago UI
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    private val calendar: Calendar
        get() {
            val calendar = Calendar.getInstance(Locale.KOREA)
            calendar.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            return calendar
        }

    val year: Int
        get() = calendar.get(Calendar.YEAR)

    val currentMonth: Int
        get() = calendar.get(Calendar.MONTH) + 1


    fun getToday() : String{
        val day = ddFormat.calendar.get(Calendar.DAY_OF_MONTH)
        return day.toString()
    }

    fun getTOdayWeek() : String{
        return wFormat.format(calendar.time)
    }

    fun monthEnd(month : Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 15)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun endOfMonth(month : Int): Int {
        calendar.set(year, month - 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun startOfMonth(month : Int): Int {
        calendar.set(year, month - 1, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun getCurrentDate(): Date {
        return calendar.time
    }

    fun getCurrentDateTimeAsLong(): String {
        return getCurrentDate().time.toString()
    }

    fun getTimeLongToString(time : Long) : String {
        hhmmFormat.timeZone = TimeZone.getTimeZone("GMT")
        return hhmmFormat.format(time)
    }

    fun getTimeLongToDD(time : Long) : String {
        ddFormat.timeZone = TimeZone.getTimeZone("GMT")
        return hhmmFormat.format(time)
    }

    fun getCurrentDateFromStringFormat(): String {
        return yyyyMMddHHmmssFormat.format(getCurrentDate().time)
    }

    fun getYearMonth() : String{
        return getFormattedDate(getCurrentDate().time, yyyyMMFormat)
    }

    fun getWeekStartDate(day: Int , month: Int): Date {
        val calendar = Calendar.getInstance(Locale.KOREA)
        calendar.set(year, month - 1, day)
        while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1)
        }
        return calendar.time
    }

    fun getWeekEndDate(day : Int , month: Int): Date {
        val calendar = Calendar.getInstance(Locale.KOREA)
        calendar.set(year, month - 1, day+1)
        while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1)
        }
        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }

    fun getCurrentWeekStartDate(): String? {
        //ensure the method works within current month
        calendar[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        System.out.println("Before Start Date " + calendar.time)
        val date = calendar.time
        val dfDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA)
        val CurrentDate = dfDate.format(date)
        println("Start Date $CurrentDate")
        return CurrentDate
    }

    fun getCurrentWeekEndDate(): String? {
        //ensure the method works within current month
        calendar[Calendar.DAY_OF_WEEK] = Calendar.SATURDAY
        System.out.println("Before End Date " + calendar.time)
        val date = calendar.time
        val dfDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val CurrentDate = dfDate.format(date)
        println("End Date $CurrentDate")
        return CurrentDate
    }


    fun getDateArrayOfDaysBetweenDates(startDate: Date, endDate: Date): List<Date> {
        val dates = ArrayList<Date>()
        val cal1 = Calendar.getInstance(Locale.KOREA)
        cal1.time = startDate
        val cal2 = Calendar.getInstance(Locale.KOREA)
        cal2.time = endDate
        while (cal1.before(cal2) || cal1.equals(cal2)) {
            dates.add(cal1.time)
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }


    fun getFormattedDate(dateTime: Long?, pattern: String): String {
        val newFormat = SimpleDateFormat(pattern , Locale.KOREA)
        return newFormat.format(Date(dateTime!!))
    }

    fun getDateTimeToString(date: Date): String {
        return MMddEFormat.format(date)
    }

    fun getDayTimeToString(date: Date?): String? {
        return ddFormat.format(date)
    }

    fun getDateRangeByNumberOfDays(date: Date?, numberOfDays: Int): Int{
        val dates: Array<Date?> = arrayOfNulls(2)
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DAY_OF_YEAR, numberOfDays)
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    //endregion time Utilities

}