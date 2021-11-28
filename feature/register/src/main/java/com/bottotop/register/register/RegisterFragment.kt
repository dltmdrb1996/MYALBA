package com.bottotop.register.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.register.R
import com.bottotop.register.databinding.FragmentRegisterBinding
import com.bottotop.register.register.alba.AlbaFragment
import com.bottotop.register.register.manager.ManagerFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment :
    BaseFragment<FragmentRegisterBinding, RegisterViewModel>(R.layout.fragment_register, "가입_프래그먼트") {

    private val vm by viewModels<RegisterViewModel>()
    override val viewModel get() = vm
    private val adapter: RegisterViewPagerAdapter by lazy {
        RegisterViewPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle
        )
    }
    override fun setBindings() {
        _binding?.viewModel = viewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init_TabLayout_Viewpager()
        initObserver()
        initClick()
    }

    override fun initObserver() {}
    override fun initClick() {}

    private fun init_TabLayout_Viewpager(){
        adapter.run {
            addItem(AlbaFragment())
            addItem(ManagerFragment())
        }
        _binding?.apply {
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout,viewPager){ tab , pos ->
                when(pos){
                    0 -> tab.text = "알바등록"
                    1 -> tab.text = "가게등록"
                }
            }.attach()
        }
    }




}