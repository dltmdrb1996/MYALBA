package com.bottotop.remote.login

import android.content.Context
import android.util.Log
import com.aryanmo.utils.utils.getResources
import com.bottotop.model.UserInfo
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
    val userInfo = UserInfo

    suspend fun checkToken(): Boolean {
        return mOAuthLoginInstance.getAccessToken(context) != null
    }

    suspend fun loadUser(): Boolean {
        val result = getNaverUser()
        val map = parseNaverUser(result)
        return map
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
                UserInfo.name = name
                UserInfo.mobile = mobile
                UserInfo.birth = birth
                UserInfo.email = email
                it.resume(true)
            }
        } catch (e: JSONException) {
            Log.e(TAG, "네이버 정보 불러오기 부분에서 오류가남")
            Log.e(TAG, "${e.message}")
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