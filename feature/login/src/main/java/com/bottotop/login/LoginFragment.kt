package com.bottotop.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.setOnSingleClickListener
import com.bottotop.core.ext.showToast
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
        observeLogin()

        _binding?.apply {
            btnKakaoLogin.setOnSingleClickListener {
                loginKakao()
            }
            btnNaverLogin.setOnSingleClickListener {
                mOAuthLoginModule.startOauthLoginActivity(requireActivity(),
                    this@LoginFragment.viewModel.getAuth())
            }
        }
        observeToast()
    }

    fun loginKakao() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(
                requireContext(),
                callback = viewModel.getKakaoCallBack()
            )
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                requireContext(),
                callback = viewModel.getKakaoCallBack()
            )
        }
    }

    fun observeLogin() {
        viewModel.token.observe(viewLifecycleOwner, {
            if (it) {
                (requireActivity() as ToFlowNavigatable).navigateToFlow(NavigationFlow.HomeFlow("test"))
            } else {
                showToast("로그인실패")
            }
        })
    }
}

@BindingAdapter("OAuthHandler")
fun setOAuthHandler(oAuthLoginButton: OAuthLoginButton, oAuthLoginHandler: OAuthLoginHandler){
    oAuthLoginButton.setOAuthLoginHandler(oAuthLoginHandler)
}
