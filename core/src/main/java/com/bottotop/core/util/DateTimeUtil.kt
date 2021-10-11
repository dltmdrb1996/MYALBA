package com.bottotop.core.util

import java.text.SimpleDateFormat
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.ParseException
import java.util.*


class DateTimeUtil {

    companion object {

        private const val DEFAULT_TIMEZONE : String = "Asia/Seoul"
        private const val DATE_TIME_PATTERN : String = "yyyy-MMM-dd HH:mm:ss"
        private const val DATE_PATTERN : String = "yyyy-MMM-dd"
        private const val PATTERN : String = "MM dd일 E요일"
        private const val TIME_PATTERN : String = "HH:mm:ss"
        private const val DAY_PATTERN : String = "dd"

        fun getCurrentDateByTimeZone(timeZone: String = DEFAULT_TIMEZONE): Date? {
            val dateFormat = SimpleDateFormat(DATE_TIME_PATTERN)
            val dateFormatConf = SimpleDateFormat(DATE_TIME_PATTERN)
            dateFormatConf.timeZone = TimeZone.getTimeZone(timeZone)
            val date = Date()
            return try {
                dateFormat.parse(dateFormatConf.format(date))
            } catch (exception: ParseException) {
                println("Unable to get TimeZone $exception")
                date
            }
        }



        fun getDateToString(date: Date?): String? {
            val formatter: DateFormat = SimpleDateFormat(DATE_PATTERN)
            return formatter.format(date)
        }

        fun getTimeToString(date: Date?): String? {
            val formatter: DateFormat = SimpleDateFormat(TIME_PATTERN)
            return formatter.format(date)
        }

        fun getDateTimeFromString(dateString: String?): Date? {
            val formatter = SimpleDateFormat(DATE_TIME_PATTERN)
            return try {
                formatter.parse(dateString)
            } catch (exception: ParseException) {
                println("Unable to get date using string date $exception")
                null
            }
        }

        fun getDateFromString(dateString: String?): Date? {
            val formatter = SimpleDateFormat(DATE_PATTERN)
            return try {
                formatter.parse(dateString)
            } catch (exception: ParseException) {
                println("Unable to get date using string date $exception")
                null
            }
        }

        fun getDateToBeginningOfDay(date: Date?): Date? {
            val cal = Calendar.getInstance()
            cal.time = date
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.time
        }

        fun getDateToEndOfDay(date: Date?): Date? {
            val cal = Calendar.getInstance()
            cal.time = date
            cal.set(Calendar.HOUR_OF_DAY, 23)
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            return cal.time
        }

        fun getDateRangeByNumberOfDays(date: Date?, numberOfDays: Int): Int{
            val dates: Array<Date?> = arrayOfNulls(2)
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.DAY_OF_YEAR, numberOfDays)
            return cal.get(Calendar.DAY_OF_MONTH)
        }

        fun convertWeek(dayOfWeek : Int) : String{
            return when(dayOfWeek){
                1 -> "일"
                2 -> "월"
                3 -> "화"
                4 -> "수"
                5 -> "목"
                6 -> "금"
                7 -> "토"
                else -> "에러"
            }
        }
    }

    fun getMonth(month: Int): String? {
        return DateFormatSymbols().getMonths().get(month - 1)
    }

}