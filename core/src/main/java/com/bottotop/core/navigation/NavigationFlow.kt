package com.bottotop.core.navigation

sealed class NavigationFlow {
    class HomeFlow(val msg: String = "none") : NavigationFlow()
    class LoginFlow(val msg: String = "none") : NavigationFlow()
    class AssetFlow(val msg: String = "none") : NavigationFlow()
    class CommunityFlow(val msg: String = "none") : NavigationFlow()
    class MemberFlow(val msg: String = "none") : NavigationFlow()
    class ScheduleFlow(val msg: String = "none") : NavigationFlow()
    class SettingFlow(val msg: String = "none") : NavigationFlow()
    class RegisterFlow(val msg: String = "none") : NavigationFlow()
}