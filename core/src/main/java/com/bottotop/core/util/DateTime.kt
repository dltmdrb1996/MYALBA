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

import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DateTime {

    //region syntax
    /*

        Pattern Syntax
        You can use the following symbols in your formatting pattern:
        
        G 	Era designator (before christ, after christ)
        y 	Year (e.g. 12 or 2012). Use either yy or yyyy.
        M 	Month in year. Number of M's determine length of format (e.g. MM, MMM or MMMMM)
        d 	Day in month. Number of d's determine length of format (e.g. d or dd)
        h 	Hour of day, 1-12 (AM / PM) (normally hh)
        H 	Hour of day, 0-23 (normally HH)
        m 	Minute in hour, 0-59 (normally mm)
        s 	Second in minute, 0-59 (normally ss)
        S 	Millisecond in second, 0-999 (normally SSS)
        E 	Day in week (e.g Monday, Tuesday etc.)
        D 	Day in year (1-366)
        F 	Day of week in month (e.g. 1st Thursday of December)
        w 	Week in year (1-53)
        W 	Week in month (0-5)
        a 	AM / PM marker
        k 	Hour in day (1-24, unlike HH's 0-23)
        K 	Hour in day, AM / PM (0-11)
        z 	Time Zone
        ' 	Escape for text delimiter
        ' 	Single quote

        Pattern 	                        Example
        dd-MM-yy 	                        31-01-12
        dd-MM-yyyy 	                        31-01-2012
        MM-dd-yyyy 	                        01-31-2012
        yyyy-MM-dd 	                        2012-01-31
        yyyy-MM-dd HH:mm:ss 	            2012-01-31 23:59:59
        yyyy-MM-dd HH:mm:ss.SSS 	        2012-01-31 23:59:59.999
        yyyy-MM-dd HH:mm:ss.SSSZ 	        2012-01-31 23:59:59.999+0100
        EEEEE MMMMM yyyy HH:mm:ss.SSSZ 	    Saturday November 2012 10:45:42.720+0100

     */
    //endregion syntax

    var yyyyMMddFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd")
    var yyyyMMddHHmmssFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    var dayStartFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd 00:00:00")
    var dayEndFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd 11:59:59")
    var yyyyFormat: DateFormat = SimpleDateFormat("yyyy")
    var MMFormat: DateFormat = SimpleDateFormat("MM")
    var ddFormat: DateFormat = SimpleDateFormat("dd")
    var ddwFormat: DateFormat = SimpleDateFormat("dd일 E요일")
    var MMddEFormat : DateFormat = SimpleDateFormat("MM dd일 E요일")
    var HHFormat: DateFormat = SimpleDateFormat("HH")
    var mmFormat: DateFormat = SimpleDateFormat("mm")
    var ampmFormat: DateFormat = SimpleDateFormat("a")
    private val TIME_PATTERN : String = "HH:mm"
    private val PATTERN : String = "MM dd일 E요일"
    private val DAY_PATTERN : String = "dd"

    var dayNameLongFormat: DateFormat = SimpleDateFormat("EEEE")
    var dayNameShortFormat: DateFormat = SimpleDateFormat("EEE")

    var monthNameLongFormat: DateFormat = SimpleDateFormat("MMMM")
    var monthNameShortFormat: DateFormat = SimpleDateFormat("MMM")

    var date = Date()

    //region timeago UI
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    private val calendar: Calendar
        get() {
            val calendar = Calendar.getInstance()
            return calendar
        }

    val year: Int
        get() = calendar.get(Calendar.YEAR)

    val currentMonth: Int
        get() = calendar.get(Calendar.MONTH) + 1

    fun getToday() : String{
        val day = ddwFormat.calendar.get(Calendar.DAY_OF_MONTH)
        return day.toString()
    }

    fun getTodayWeek() : String{
        val formatter : DateFormat = ddwFormat
        return formatter.format(calendar.time)
    }

    fun monthEnd(month : Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 15)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun endOfMonth(month : Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun startOfMonth(month : Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun getCurrentDate(): Date {
        return Date()
    }

    fun getCurrentDateTimeAsLong(): String {
        return Date().time.toString()
    }

    fun getTimeLongToString(time : Long) : String {
        return getFormattedDate(time, TIME_PATTERN)
    }
    fun getCurrentDateFromStringFormat(): String {
        return getFormattedDate(getCurrentDate().time, TIME_PATTERN)
    }

    fun getWeekStartDate(day: Int , month: Int): Date? {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1)
        }
        return calendar.time
    }

    fun getWeekEndDate(day : Int , month: Int): Date? {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day+1)
        while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1)
        }
        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }

    fun getCurrentWeekStartDate(): String? {
        val c = Calendar.getInstance()
        //ensure the method works within current month
        c[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        System.out.println("Before Start Date " + c.time)
        val date = c.time
        val dfDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val CurrentDate = dfDate.format(date)
        println("Start Date $CurrentDate")
        return CurrentDate
    }

    fun getCurrentWeekEndDate(): String? {
        val c = Calendar.getInstance()
        //ensure the method works within current month
        c[Calendar.DAY_OF_WEEK] = Calendar.SATURDAY
        System.out.println("Before End Date " + c.time)
        val date = c.time
        val dfDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val CurrentDate = dfDate.format(date)
        println("End Date $CurrentDate")
        return CurrentDate
    }


    fun getDateArrayOfDaysBetweenDates(startDate: Date?, endDate: Date?): List<Date>? {
        val dates = ArrayList<Date>()
        val cal1 = Calendar.getInstance()
        cal1.time = startDate
        val cal2 = Calendar.getInstance()
        cal2.time = endDate
        while (cal1.before(cal2) || cal1.equals(cal2)) {
            dates.add(cal1.time)
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }


    fun getFormattedDate(dateTime: Long?, pattern: String): String {
        val newFormat = SimpleDateFormat(pattern)
        return newFormat.format(Date(dateTime!!))
    }

    fun getDateTimeToString(date: Date?): String? {
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