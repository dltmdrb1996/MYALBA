package com.bottotop.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.core.model.LoginState
import com.bottotop.core.util.DateTime
import com.bottotop.model.User
import com.bottotop.model.query.SetScheduleQuery
import com.bottotop.model.repository.DataRepository
import com.bottotop.model.repository.SocialLoginRepository
import com.kakao.sdk.auth.model.OAuthToken
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val socialLoginRepository: SocialLoginRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val dataRepository: DataRepository,
    @ApplicationContext context: Context
) : BaseViewModel("로그인뷰모델") {

    private val context = context
    private val mOAuthLoginModule = OAuthLogin.getInstance()

    lateinit var user : User

    private val _login = MutableLiveData<LoginState>()
    val login : LiveData<LoginState> = _login

    var userFlag = false

    fun getUser(){
        viewModelScope.launch(dispatcherProvider.io) {
            user = dataRepository.getUser(SocialInfo.id)
        }
    }

    private val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                SocialInfo.social = "naver"
                if (userFlag) {
                    if (user.company == "null") _login.postValue(LoginState.NoCompany)
                    else initInfo()
                }
                else _login.postValue(LoginState.Register)
            } else {
                val errorCode = mOAuthLoginModule.getLastErrorCode(context).code
                val errorDesc = mOAuthLoginModule.getLastErrorDesc(context)
                _login.postValue(LoginState.NoData)
                Log.e(TAG, "NaverLoginHandle : ${errorCode} , ${errorDesc}", )
            }
        }
    }

    fun initInfo(){
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io){
            dataRepository.setSchedule(SetScheduleQuery(SocialInfo.id , DateTime().getYearMonth() , user.company))
            val refreshCompanies = getAPIResult(dataRepository.refreshCompanies(user.company),
                "$TAG : refreshCompanies"
            )
            handleLoading(false)
            if(refreshCompanies) _login.postValue(LoginState.Success)
        }
    }

    fun getAuth() = mOAuthLoginHandler


//
//    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//        if (error != null) {
//            _login.postValue(LoginState.NoData)
//            Log.e(TAG, "로그인 실패", error)
//        } else if (token != null) {
//            SocialInfo.social = "kakao"
//            if (userFlag) {
//                if (user.company == "null") {
//                    _login.postValue(LoginState.NoCompany)
//                } else{
//                    _login.postValue(LoginState.Success)
//                }
//            } else {
//                _login.postValue(LoginState.Register)
//            }
//        }
//    }
//

//    fun getKakaoCallBack() = callback

    override fun onCleared() {
        super.onCleared()
        Log.e(TAG, "onCleared: 종료", )
    }

}

