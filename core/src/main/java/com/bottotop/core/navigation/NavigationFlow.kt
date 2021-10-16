package com.bottotop.core.navigation

sealed class NavigationFlow {
    class HomeFlow(val msg: String) : NavigationFlow()
    class LoginFlow(val msg: String) : NavigationFlow()
    class AssetFlow(val msg: String) : NavigationFlow()
    class ChatFlow(val msg: String) : NavigationFlow()
    class CommunityFlow(val msg: String) : NavigationFlow()
    class MemberFlow(val msg: String) : NavigationFlow()
    class ScheduleFlow(val msg: String) : NavigationFlow()
    class SettingFlow(val msg: String) : NavigationFlow()
    class MainFlow(val msg: String) : NavigationFlow()
    class RegisterFlow(val msg: String) : NavigationFlow()
}