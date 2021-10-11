package com.bottotop.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.isInvisible
import com.bottotop.core.ext.isVisible
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigatable
import com.bottotop.core.util.addOnAnimationListener
import com.bottotop.core.util.withDelayOnMain
import com.bottotop.splash.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashBinding, SplashViewModel>(R.layout.fragment_splash, "스플래쉬") {

    override val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: test")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setBindings() {
        _binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachSplashAnimationCompleteListener()
    }

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
            observeToken()
        }
    }

    fun observeToken(){
        viewModel.token.observe(viewLifecycleOwner,{
            if(it){
                (requireActivity() as ToFlowNavigatable).navigateToFlow(NavigationFlow.HomeFlow("test"))
            }else{
                (requireActivity() as ToFlowNavigatable).navigateToFlow(NavigationFlow.LoginFlow("test"))
            }
        })
    }


}