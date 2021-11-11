package com.bottotop.core.global

object NavigationRouter {
    lateinit var currentState : NavigationTable
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
            "register" -> currentState=NavigationTable.Register
            "onBoarding" -> currentState=NavigationTable.OnBoarding
            "memberDetail" -> currentState=NavigationTable.MemberDetail
            "info" -> currentState=NavigationTable.Info
            "setting" -> currentState=NavigationTable.Setting
            "communityDetail" -> currentState=NavigationTable.CommunityDetail
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
    object Register : NavigationTable()
    object OnBoarding : NavigationTable()
    object MemberDetail : NavigationTable()
    object Info : NavigationTable()
    object Setting : NavigationTable()
    object CommunityDetail : NavigationTable()
}