package com.bottotop.core.navigation

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

fun buildDeepLink(destination: DeepLinkDestination) =
    NavDeepLinkRequest.Builder
        .fromUri(destination.address.toUri())
        .build()

fun NavController.deepLinkNavigateTo(
    deepLinkDestination: DeepLinkDestination,
    popUpTo: Boolean = false
) {
    val builder = NavOptions.Builder()

    if (popUpTo) {
        builder.setPopUpTo(graph.startDestination, true)
    }

    navigate(
        buildDeepLink(deepLinkDestination),
        builder.build()
    )
}

sealed class DeepLinkDestination(val address: String) {
    class Home(msg: String) : DeepLinkDestination("example://home/exampleArgs?msg={msg}")
    class Login(msg: String) : DeepLinkDestination("example://login/exampleArgs?msg={msg}")
    class Asset(msg: String) : DeepLinkDestination("example://asset/exampleArgs?msg={msg}")
    class Chat(msg: String) : DeepLinkDestination("example://chat/exampleArgs?msg={msg}")
    class Community(msg: String) : DeepLinkDestination("example://community/exampleArgs?msg={msg}")
    class Member(msg: String) : DeepLinkDestination("example://member/exampleArgs?msg={msg}")
    class Schedule(msg: String) : DeepLinkDestination("example://schedule/exampleArgs?msg={msg}")
    class Setting(msg: String) : DeepLinkDestination("example://setting/exampleArgs?msg={msg}")
    class Main(msg: String) : DeepLinkDestination("example://main/exampleArgs?msg={msg}")
    class ScheduleDetail(msg: String) : DeepLinkDestination("example://scheduleDetail/exampleArgs?msg={msg}")
}

//navigate_to_next_deeplink.setOnClickListener {
//    findNavController().deepLinkNavigateTo(DeepLinkDestination.Next)
//}

//it.post {
//    findNavController().deepLinkNavigateTo(DeepLinkDestination.Dashboard("From next fragment deeplink"))
//}