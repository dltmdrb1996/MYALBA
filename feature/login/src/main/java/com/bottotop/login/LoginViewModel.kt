package com.bottotop.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.model.LoginFlag
import com.bottotop.model.UserInfo
import com.bottotop.model.repository.LoginRepository
import com.kakao.sdk.auth.model.OAuthToken
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val dispatcherProvider: DispatcherProvider,
    @ApplicationContext context: Context
) : BaseViewModel("로그인뷰모델") {

    private val context = context
    private val mOAuthLoginModule = OAuthLogin.getInstance()

    private val _movie = MutableLiveData<String>()
    val movie: LiveData<String> = _movie

    private val _token = MutableLiveData<Boolean>()
    val token : LiveData<Boolean> = _token

    init {
    }
    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            _token.postValue(false)
            Log.e(TAG, "로그인 실패", error)
        } else if (token != null) {
            UserInfo.login_flag = LoginFlag.Kakao
            _token.postValue(true)
        }
    }

    val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                val accessToken = mOAuthLoginModule.getAccessToken(context)
                val refreshToken = mOAuthLoginModule.getRefreshToken(context)
                val expiresAt = mOAuthLoginModule.getExpiresAt(context)
                val tokenType = mOAuthLoginModule.getTokenType(context)
                UserInfo.login_flag = LoginFlag.Naver
                _token.postValue(true)
            } else {
                val errorCode = mOAuthLoginModule.getLastErrorCode(context).code
                val errorDesc = mOAuthLoginModule.getLastErrorDesc(context)
                _token.postValue(false)
                Log.e(TAG, "NaverLoginHandle : ${errorCode} , ${errorDesc}", )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e(TAG, "onCleared: 종료", )
    }
    fun getAuth() = mOAuthLoginHandler
    fun getKakaoCallBack() = callback

}

