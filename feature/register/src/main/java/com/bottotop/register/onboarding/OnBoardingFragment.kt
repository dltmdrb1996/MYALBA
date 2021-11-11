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
import dagger.hilt.android.AndroidEntryPoint

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
        if(navArg.msg == "noCompany"){
            findNavController().navigate(OnBoardingFragmentDirections.actionToRegister())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewpager()
        observeLoading()
        observeSuccess()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

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

    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })
    }

    private fun observeSuccess(){
        viewModel.success.observe(viewLifecycleOwner,{
            if(it){
                findNavController().navigate(OnBoardingFragmentDirections.actionToRegister())
            }else{
                showToast("계정생성에 실패했습니다.")
            }
        })
    }

//    private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
//        val myFragment = childFragmentManager.findFragmentByTag("f$position")
//        myFragment?.view?.let { updatePagerHeightForChild(it,binding.viewPager) }
//        view.post {
//            val wMeasureSpec =
//                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
//            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//            view.measure(wMeasureSpec, hMeasureSpec)
//
//            if (pager.layoutParams.height != view.measuredHeight) {
//                pager.layoutParams = (pager.layoutParams)
//                    .also { lp ->
//                        lp.height = view.measuredHeight
//                    }
//            }
//        }
//    }


}