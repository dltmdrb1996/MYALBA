package com.bottotop.core.navigation

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bottotop.model.Community

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
    class Home(msg: String = "none") : DeepLinkDestination("example://home/exampleArgs?msg=${msg}")
    class Login(msg: String = "none") : DeepLinkDestination("example://login/exampleArgs?msg=${msg}")
    class Asset(msg: String = "none") : DeepLinkDestination("example://asset/exampleArgs?msg=${msg}")
    class Community(msg: String = "none") : DeepLinkDestination("example://community/exampleArgs?msg=${msg}")
    class Member(msg: String = "none") : DeepLinkDestination("example://member/exampleArgs?msg=${msg}")
    class Schedule(msg: String = "none") : DeepLinkDestination("example://schedule/exampleArgs?msg=${msg}")
    class Setting(msg: String = "none") : DeepLinkDestination("example://setting/SettingFragment?msg=${msg}")
    class ScheduleDetail(msg: String = "none") : DeepLinkDestination("example://scheduleDetail/exampleArgs?msg=${msg}")
    class Register(msg:String = "none") : DeepLinkDestination("example://Register/RegisterFragmentArgs?msg=${msg}")
    class MemberDetail(msg:String = "none") : DeepLinkDestination("example://memberDetail/MemberDetailFragmentArgs?msg=${msg}")
    class CommunityDetail(msg: String = "none") : DeepLinkDestination("example://communityDetail/CommunityDetailFragmentArgs?msg=${msg}")
    class Info(msg: String = "none") : DeepLinkDestination("example://Info/InfoFragmentArgs?msg=${msg}")
    class Notification(msg: String = "none") : DeepLinkDestination("example://Notification/NotificationFragmentArgs?msg=${msg}")

}

//navigate_to_next_deeplink.setOnClickListener {
//    findNavController().deepLinkNavigateTo(DeepLinkDestination.Next)
//}

//it.post {
//    findNavController().deepLinkNavigateTo(DeepLinkDestination.Dashboard("From next fragment deeplink"))
//}