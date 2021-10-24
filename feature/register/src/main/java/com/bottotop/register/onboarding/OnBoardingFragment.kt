package com.bottotop.register.onboarding

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.isInvisible
import com.bottotop.core.ext.isVisible
import com.bottotop.core.ext.setOnSingleClickListener
import com.bottotop.register.R
import com.bottotop.register.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment :
    BaseFragment<FragmentOnboardingBinding, OnBoardingViewModel>(
        R.layout.fragment_onboarding,
        "온보딩_프래그먼트"
    ) {

    private val navArg : OnBoardingFragmentArgs by navArgs()

    private val vm by viewModels<OnBoardingViewModel>()
    private val adapter: OnBoardingAdapter by lazy {
        OnBoardingAdapter()
    }

    override val viewModel get() = vm
    override fun setBindings() {
        _binding?.viewModel = viewModel
        _binding?.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: ${navArg.toString()}", )
        if(navArg.msg == "noCompany"){
            Log.e(TAG, "onCreate: 43536346", )
            findNavController().navigate(OnBoardingFragmentDirections.actionToRegister())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewpager()
        observeToast()
        observeLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun initViewpager() {
        _binding?.apply {
            val viewpager = this.viewPager
            viewPager.registerOnPageChangeCallback(PageChangeCallback())
            onboardNext.setOnSingleClickListener {
                if (viewpager.currentItem == 3) {
                    this@OnBoardingFragment.viewModel.setUser()
                    findNavController().navigate(OnBoardingFragmentDirections.actionToRegister())
                }
                else{
                    viewPager.setCurrentItem(viewpager.currentItem + 1)
                }
            }
            onboardBack.setOnSingleClickListener {
                viewPager.setCurrentItem(viewpager.currentItem - 1)
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

    fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })
    }
}