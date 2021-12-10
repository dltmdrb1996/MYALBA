package com.bottotop.myalba

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bottotop.core.global.PreferenceHelper
import com.bottotop.core.global.PreferenceHelper.get
import com.bottotop.core.global.PreferenceHelper.set
import com.bottotop.model.Notification
import com.bottotop.model.repository.DataRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService (): FirebaseMessagingService() {
    val job = SupervisorJob()

    private var isAppInForeground = false
    lateinit var mPref : SharedPreferences

    @Inject lateinit var dataRepository: DataRepository
    override fun onCreate() {
        super.onCreate()
        mPref = PreferenceHelper.defaultPrefs(applicationContext)
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    fun isForegrounded(): Boolean {
        val appProcessInfo = RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(appProcessInfo)
        return appProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND ||
                appProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE
    }

    fun setTitle(from : String) : String{
        val topic = from.split('/').last()
        return when(topic){
            "update" -> "업데이트 내역이 있습니다."
            "notice" -> "공지사항"
            else -> "커뮤니티에 새 메시지가 있습니다."
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = setTitle(remoteMessage.from!!)
        val content = remoteMessage.data["body"]!!
        val time = remoteMessage.sentTime
        val badge : Int = mPref["badge" , 0]
        mPref["badge"] = badge+1
        CoroutineScope(job).launch {
            val notification = Notification(title,time,content)
            dataRepository.insertNotification(notification)
        }

        if (remoteMessage.data.isNotEmpty() && !isForegrounded() ) {
            sendNotification(title , content)
        }
    }

    override fun onNewToken(token: String) {
        Timber.e("Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        Timber.e("sendRegistrationTokenToServer($token)")
    }

    private fun sendNotification(title : String , body: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }


}