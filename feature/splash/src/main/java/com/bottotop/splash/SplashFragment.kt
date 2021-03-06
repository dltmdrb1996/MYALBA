package com.bottotop.splash

import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.isInvisible
import com.bottotop.core.ext.isVisible
import com.bottotop.core.ext.withDelayOnMain
import com.bottotop.core.global.SocialInfo
import com.bottotop.core.model.LoginState
import com.bottotop.core.navigation.DeepLinkDestination
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigation
import com.bottotop.core.navigation.deepLinkNavigateTo
import com.bottotop.core.util.addOnAnimationListener
import com.bottotop.splash.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashBinding, SplashViewModel>(R.layout.fragment_splash, "스플래쉬") {

    override val viewModel: SplashViewModel by viewModels()

    override fun setBindings() {
        _binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachSplashAnimationCompleteListener()
        initObserver()
        initClick()

    }

    override fun initObserver() {}
    override fun initClick() {}

    private fun attachSplashAnimationCompleteListener() {
        _binding?.apply {
            splashLogoLottieView.apply {
                addOnAnimationListener(
                    onAnimationEnd = {
                        showBackgroundAnimation(
                            showResearchScreenWithDelayAndSharedTransition()
                        )
                    }
                )
            }
        }
    }

    private fun showBackgroundAnimation(onAnimationEnd: () -> Unit) {
        _binding?.apply {
            backgroundView.apply {
                val cx = this.measuredWidth / 2
                val cy = this.measuredHeight / 2
                val finalRadius = max(this.width, this.height) / 2
                ViewAnimationUtils.createCircularReveal(this, cx, cy, 0f, finalRadius.toFloat())
                    .let {
                        this.visibility = View.VISIBLE
                        it.start()
                        it.addOnAnimationListener(onAnimationEnd = {
                            onAnimationEnd()
                        })
                    }
            }
        }
    }

    private fun showResearchScreenWithDelayAndSharedTransition(): () -> Unit = {
        _binding?.splashLogoImageView?.isVisible()
        _binding?.splashLogoLottieView?.isInvisible()
        withDelayOnMain(100) {
            observeLogin()
        }
    }

    private fun observeLogin() {
        viewModel.login.observe(viewLifecycleOwner, {
            when (it) {
                LoginState.Success -> (requireActivity() as ToFlowNavigation).navigateToFlow(
                    NavigationFlow.HomeFlow("home")
                )
                LoginState.Register -> (requireActivity() as ToFlowNavigation).navigateToFlow(
                    NavigationFlow.RegisterFlow("first")
                )
                LoginState.NoCompany -> findNavController().deepLinkNavigateTo(DeepLinkDestination.Register("none"))
                LoginState.NoData -> (requireActivity() as ToFlowNavigation).navigateToFlow(
                    NavigationFlow.LoginFlow("noData")
                )
            }
        })
    }


}