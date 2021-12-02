package com.bottotop.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.PreferenceHelper
import com.bottotop.core.global.PreferenceHelper.set
import com.bottotop.core.global.SocialInfo
import com.bottotop.core.model.LoginState
import com.bottotop.core.util.DateTime
import com.bottotop.model.User
import com.bottotop.model.query.SetScheduleQuery
import com.bottotop.model.repository.DataRepository
import com.bottotop.model.repository.SocialLoginRepository
import com.bottotop.model.wrapper.APIError
import com.bottotop.model.wrapper.APIResult
import com.kakao.sdk.common.model.ApiError
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val socialLoginRepository: SocialLoginRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val dataRepository: DataRepository,
    @ApplicationContext context: Context
) : BaseViewModel("로그인뷰모델") {

    private val mOAuthLoginModule = OAuthLogin.getInstance()

    lateinit var user : User
    private val mPref = PreferenceHelper.defaultPrefs(context)

    private val _login = MutableLiveData<LoginState>()
    val login : LiveData<LoginState> = _login

    private val naverLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                mPref["social"]="naver"
                initSocialInfo()
            } else {
                val errorCode = mOAuthLoginModule.getLastErrorCode(context).code
                val errorDesc = mOAuthLoginModule.getLastErrorDesc(context)
                _login.postValue(LoginState.NoData)
                Timber.e("NaverLoginHandle : $errorCode , $errorDesc")
            }
        }
    }

    fun initSocialInfo(){
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io){
            val infoSuccess = socialLoginRepository.getNaverInfo()
            if(infoSuccess){
                mPref["id"] = SocialInfo.id
                val getUserSuccess = dataRepository.refreshUser(SocialInfo.id)
                when(getUserSuccess){

                    APIResult.Success -> {
                        user = dataRepository.getUser(SocialInfo.id)
                        if(user.company != "null") dataRepository.refreshCompanies(user.company).result(Throwable().stackTrace).let {
                            if(it) _login.postValue(LoginState.Success)
                        }
                        else _login.postValue(LoginState.NoCompany)
                    }

                    is APIResult.Error -> {
                        when(getUserSuccess.error){
                            APIError.KeyValueError -> _login.postValue(LoginState.Register)
                            else -> showToast("에러가 발생했습니다.")
                        }
                    }

                }
            } else {
                showToast("네이버 정보를 불러오는데 실패했습니다. \n 잠시 후 다시 시도해주세요")
            }
            handleLoading(false)
        }
    }

    fun getAuth() = naverLoginHandler


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

}

