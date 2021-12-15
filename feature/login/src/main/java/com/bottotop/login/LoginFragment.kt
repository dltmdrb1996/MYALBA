package com.bottotop.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.setOnSingleClickListener
import com.bottotop.core.ext.showSnackbar
import com.bottotop.core.ext.showToast
import com.bottotop.core.model.LoginState
import com.bottotop.core.navigation.DeepLinkDestination
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigation
import com.bottotop.core.navigation.deepLinkNavigateTo
import com.bottotop.login.databinding.FragmentLoginBinding
import com.nhn.android.naverlogin.OAuthLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment :
    BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login, "로그인_프래그먼트") {

    private val navArg : LoginFragmentArgs by navArgs()

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
        initObserver()
        initClick()
    }

    override fun initObserver() {
        observeLogin()
    }

    override fun initClick() {
        _binding?.apply {
            btnKakaoLogin.setOnSingleClickListener {
                showSnackbar("사업자등록을 해야 정보를제공해주어 막아두었습니다.",3000)
            }
            btnNaverLogin.setOnSingleClickListener {
                mOAuthLoginModule.startOauthLoginActivity(requireActivity(), this@LoginFragment.viewModel.getAuth())
            }
        }
    }

    private fun observeLogin(){
        viewModel.login.observe(viewLifecycleOwner,{
            when(it){
                LoginState.Success -> (requireActivity() as ToFlowNavigation).navigateToFlow(NavigationFlow.HomeFlow("home"))
                LoginState.Register -> (requireActivity() as ToFlowNavigation).navigateToFlow(NavigationFlow.RegisterFlow("first"))
                LoginState.NoCompany -> findNavController().deepLinkNavigateTo(DeepLinkDestination.Register("none"))
                LoginState.NoData -> showToast("로그인시도가 실패했습니다 다시시도해주세요.")
                else -> showToast("에러가 발생했습니다.")
            }
        })
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

}



