package com.bottotop.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.setOnSingleClickListener
import com.bottotop.core.ext.showSnackbar
import com.bottotop.core.ext.showToast
import com.bottotop.core.model.LoginState
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigatable
import com.bottotop.login.databinding.FragmentLoginBinding
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment :
    BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login, "로그인_프래그먼트") {

    private val vm by viewModels<LoginViewModel>()
    override val viewModel get() = vm
    private val mOAuthLoginModule = OAuthLogin.getInstance()

    override fun setBindings() {
        _binding?.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToken()
        setBtn()
        observeToast()
    }

    fun setBtn(){
        _binding?.apply {
            btnKakaoLogin.setOnSingleClickListener {
                showSnackbar("사업자등록을 해야 정보를제공해주어 막아두었습니다.",3000)
            }
            btnNaverLogin.setOnSingleClickListener {
                mOAuthLoginModule.startOauthLoginActivity(requireActivity(),
                    this@LoginFragment.viewModel.getAuth())
            }
        }
    }

//    fun loginKakao() {
//        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
//            UserApiClient.instance.loginWithKakaoTalk(
//                requireContext(),
//                callback = viewModel.getKakaoCallBack()
//            )
//        } else {
//            UserApiClient.instance.loginWithKakaoAccount(
//                requireContext(),
//                callback = viewModel.getKakaoCallBack()
//            )
//        }
//    }

    fun observeToken(){
        viewModel.login.observe(viewLifecycleOwner,{
            when(it){
                LoginState.Suceess -> (requireActivity() as ToFlowNavigatable).navigateToFlow(NavigationFlow.HomeFlow("home"))
                LoginState.Register -> (requireActivity() as ToFlowNavigatable).navigateToFlow(NavigationFlow.RegisterFlow("first"))
                LoginState.NoCompany -> (requireActivity() as ToFlowNavigatable).navigateToFlow(NavigationFlow.RegisterFlow("noCompany"))
                LoginState.NoToken -> showToast("로그인시도가 실패했습니다.")
            }
        })
    }
}

//@BindingAdapter("OAuthHandler")
//fun setOAuthHandler(oAuthLoginButton: OAuthLoginButton, oAuthLoginHandler: OAuthLoginHandler){
//    oAuthLoginButton.setOAuthLoginHandler(oAuthLoginHandler)
//}
