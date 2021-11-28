package com.bottotop.myalba

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.nhn.android.naverlogin.OAuthLogin
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


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