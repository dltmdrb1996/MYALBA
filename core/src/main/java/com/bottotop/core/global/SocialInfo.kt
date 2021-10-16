package com.bottotop.core.global

object SocialInfo{

//    private const val MONDAY : Int = 0
//    private const val TUESDAY : Int = 1
//    private const val WEDNESDAY : Int = 2
//    private const val THURSDAY : Int = 3
//    private const val FRIDAY : Int = 4
//    private const val SATURDAY : Int = 5
//    private const val SUNDAY : Int = 6

    var id : String = ""
    var social : String = ""
    var name : String = ""
    var email : String = ""
    var tel : String = ""
    var birth : String = ""

}

//sealed class LoginFlag(){
//    object Kakao : LoginFlag()
//    object Naver : LoginFlag()
//}

sealed class WeekDay(){
    object MONDAY : WeekDay()
    object TUESDAY : WeekDay()
    object WEDNESDAY : WeekDay()
    object THURSDAY : WeekDay()
    object FRIDAY : WeekDay()
    object SATURDAY : WeekDay()
    object SUNDAY : WeekDay()
}


