package com.bottotop.model

object UserInfo{

//    private const val MONDAY : Int = 0
//    private const val TUESDAY : Int = 1
//    private const val WEDNESDAY : Int = 2
//    private const val THURSDAY : Int = 3
//    private const val FRIDAY : Int = 4
//    private const val SATURDAY : Int = 5
//    private const val SUNDAY : Int = 6

    var android_Id : String = ""
    lateinit var login_flag : LoginFlag
    var name : String = ""
    var email : String = ""
    var mobile : String = ""
    var age : String = ""
    var birth : String = ""
    var scheduleInfo : ScheduleInfo = ScheduleInfo("이승규" , "08" , "15")
    var workWeek : List<WeekDay> = listOf(WeekDay.MONDAY , WeekDay.TUESDAY ,WeekDay.WEDNESDAY)

}

sealed class LoginFlag(){
    object Kakao : LoginFlag()
    object Naver : LoginFlag()
}

sealed class WeekDay(){
    object MONDAY : WeekDay()
    object TUESDAY : WeekDay()
    object WEDNESDAY : WeekDay()
    object THURSDAY : WeekDay()
    object FRIDAY : WeekDay()
    object SATURDAY : WeekDay()
    object SUNDAY : WeekDay()
}


