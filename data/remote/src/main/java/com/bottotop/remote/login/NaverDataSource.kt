package com.bottotop.remote.login

import android.content.Context
import android.util.Log
import com.bottotop.core.ext.Logg
import com.bottotop.core.global.PreferenceHelper
import com.bottotop.core.global.PreferenceHelper.set
import com.bottotop.core.global.SocialInfo
import com.nhn.android.naverlogin.OAuthLogin
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NaverDataSource @Inject constructor(@ApplicationContext context: Context) {

    private val mOAuthLoginInstance = OAuthLogin.getInstance()
    private val context = context
    val userInfo = SocialInfo
    private val mPref = PreferenceHelper.defaultPrefs(context)

    suspend fun checkToken(): Boolean {
        return mOAuthLoginInstance.getAccessToken(context) != null
    }

    suspend fun loadUser(): Boolean {
        val result = getNaverUser()
        return parseNaverUser(result)
    }

    private suspend fun getNaverUser(): String =
        withContext(Dispatchers.IO) { // to run code in Background Thread
            val url = "https://openapi.naver.com/v1/nid/me"
            val at = OAuthLogin.getInstance().getAccessToken(context)
            return@withContext OAuthLogin.getInstance().requestApi(context, at, url)
        }

    suspend fun parseNaverUser(result: String): Boolean = suspendCoroutine {
        try {
            val loginResult = JSONObject(result)
            val response: JSONObject
            if (loginResult.getString("resultcode") == "00") {
                response = loginResult.getJSONObject("response")
                val id = response.getString("id")
                val email = response.getString("email")
                val mobile = response.getString("mobile")
                val birth = response.getString("birthyear")
                val name = response.getString("name")
                mPref["id"] = id
                SocialInfo.name = name
                SocialInfo.tel = mobile
                SocialInfo.birth = birth
                SocialInfo.email = email
                SocialInfo.id = id
                SocialInfo.social="naver"
                it.resume(true)
            }
        } catch (e: JSONException) {
            Log.e(TAG,"${e.message}")
            it.resume(false)
        }
    }

    suspend fun logout() {
        mOAuthLoginInstance.logout(context)
    }

    suspend fun disConnect() {
        val isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(context)
        if (!isSuccessDeleteToken) {
            Log.d(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(context))
            Log.d(TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(context))
        }
    }

    suspend fun refresh() {
        mOAuthLoginInstance.refreshAccessToken(context)
    }

    companion object {
        val TAG = "NaverDataSource"
    }
}