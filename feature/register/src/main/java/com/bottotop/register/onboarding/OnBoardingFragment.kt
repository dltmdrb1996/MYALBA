package com.bottotop.register.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.*
import com.bottotop.register.R
import com.bottotop.register.databinding.FragmentOnboardingBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OnBoardingFragment :
    BaseFragment<FragmentOnboardingBinding, OnBoardingViewModel>(
        R.layout.fragment_onboarding,
        "온보딩_프래그먼트"
    ) {

    private val navArg : OnBoardingFragmentArgs by navArgs()

    private val vm by viewModels<OnBoardingViewModel>()
    override val viewModel get() = vm

    private val adapter: OnBoardingAdapter by lazy {
        OnBoardingAdapter(viewModel)
    }

    override fun setBindings() {
        _binding?.viewModel = viewModel
        _binding?.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(navArg.msg == "noCompany") findNavController().navigate(OnBoardingFragmentDirections.actionToRegister())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewpager()
        initObserver()
        initClick()
    }

    override fun initObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })

        viewModel.success.observe(viewLifecycleOwner,{
            if(it) findNavController().navigate(OnBoardingFragmentDirections.actionToRegister())
            else showToast("계정생성에 실패했습니다.")
        })

    }

    override fun initClick() {}

    private fun initViewpager() {
        _binding?.apply {
            val viewpager = this.viewPager
            viewPager.registerOnPageChangeCallback(PageChangeCallback())
            onboardNext.setOnClickListener {
                if (viewpager.currentItem == 3) this@OnBoardingFragment.viewModel.setUser()
                else viewPager.currentItem = viewpager.currentItem + 1
            }
            onboardBack.setOnClickListener {
                viewPager.currentItem = viewpager.currentItem - 1
            }
        }
    }

    private inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            _binding?.apply {
                when (position) {
                    0 -> onboardBack.isInvisible()
                    3 -> onboardNext.text = "등록하러가기"
                    else -> {
                        onboardBack.isVisible()
                        onboardNext.text = "Next"
                    }
                }
            }
        }
    }



}