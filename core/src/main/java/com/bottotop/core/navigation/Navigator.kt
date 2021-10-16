package com.bottotop.core.navigation

import android.util.Log
import androidx.navigation.NavController
import com.bottotop.core.MainNavGraphDirections

class Navigator {
    lateinit var navController: NavController

    // navigate on main thread or nav component crashes sometimes
    fun navigateToFlow(navigationFlow: NavigationFlow) = when (navigationFlow) {
        is NavigationFlow.HomeFlow -> navController
            .navigate(MainNavGraphDirections.actionGlobalHomeFlow(navigationFlow.msg))
        is NavigationFlow.LoginFlow -> navController
            .navigate(MainNavGraphDirections.actionGlobalLoginFlow(navigationFlow.msg))
        is NavigationFlow.AssetFlow -> navController
            .navigate(MainNavGraphDirections.actionGlobalAssetFlow(navigationFlow.msg))
        is NavigationFlow.ChatFlow -> navController
            .navigate(MainNavGraphDirections.actionGlobalChatFlow(navigationFlow.msg))
        is NavigationFlow.MemberFlow -> navController
            .navigate(MainNavGraphDirections.actionGlobalMemberFlow(navigationFlow.msg))
        is NavigationFlow.ScheduleFlow -> navController
            .navigate(MainNavGraphDirections.actionGlobalScheduleFlow(navigationFlow.msg))
        is NavigationFlow.SettingFlow -> navController
            .navigate(MainNavGraphDirections.actionGlobalSettingFlow(navigationFlow.msg))
        is NavigationFlow.CommunityFlow -> navController
            .navigate(MainNavGraphDirections.actionGlobalCommunityFlow(navigationFlow.msg))
        is NavigationFlow.MainFlow -> navController
            .navigate(MainNavGraphDirections.actionGlobalMainFlow(navigationFlow.msg))
        is NavigationFlow.RegisterFlow -> navController
            .navigate(MainNavGraphDirections.actionGlobalRegisterFlow(navigationFlow.msg))
    }
}