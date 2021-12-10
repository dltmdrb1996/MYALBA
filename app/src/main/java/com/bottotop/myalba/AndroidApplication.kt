package com.bottotop.myalba

import android.app.Application
import android.content.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.kakao.sdk.common.KakaoSdk
import com.nhn.android.naverlogin.OAuthLogin
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class AndroidApplication : Application() {
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_appKey))

        OAuthLogin.getInstance().init(
            this, "xG6HW_8DruKgHlzNS1dU", "SNVEJEUem8", "내알빠"
        )

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }


}