package com.bottotop.model

import java.util.*

object NavigationRouter {
//    private const val SPLASH_DESTINATION : Int = 2131296742
//    private const val LOGIN_DESTINATION : Int = 2131296548
//    private const val MEMBER_DESTINATION : Int = 2131296578
//    private const val COMMUNITY_DESTINATION : Int = 2131296416
//    private const val HOME_DESTINATION : Int = 2131296507
//    private const val SCHEDULE_DESTINATION : Int = 2131296689
//    private const val ASSET_DESTINATION : Int = 2131296352
//    private const val SCHEDULEDETAIL_DESTINATION = 2131296688

    lateinit var currentState : NavigationTable
    private var addToBackStack: Boolean = true
    private lateinit var fragmentBackStack: Stack<Int>

    fun saveState(destination : String){
        when(destination){
            "splash" -> currentState=NavigationTable.Splash
            "login" -> currentState= NavigationTable.Login
            "member" -> currentState=NavigationTable.Member
            "community" -> currentState=NavigationTable.Community
            "home" -> currentState=NavigationTable.Home
            "schedule" -> currentState=NavigationTable.Schedule
            "asset" -> currentState=NavigationTable.Asset
            "scheduleDetail" -> currentState=NavigationTable.ScheduleDetail
        }
    }
}

sealed class NavigationTable(){
    object Splash : NavigationTable()
    object Login : NavigationTable()
    object Member : NavigationTable()
    object Community : NavigationTable()
    object Home : NavigationTable()
    object Schedule : NavigationTable()
    object Asset : NavigationTable()
    object ScheduleDetail : NavigationTable()
}