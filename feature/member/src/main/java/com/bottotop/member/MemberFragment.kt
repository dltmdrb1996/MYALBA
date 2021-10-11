package com.bottotop.member

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.setOnSingleClickListener
import com.bottotop.core.ext.showToast
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigatable
import com.bottotop.member.databinding.FragmentMemberBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberFragment :
    BaseFragment<FragmentMemberBinding, MemberViewModel>(R.layout.fragment_member, "맴버_프래그먼트") {

    private val vm by viewModels<MemberViewModel>()
    override val viewModel get() = vm

    override fun setBindings() {
        _binding?.viewModel = viewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoading()
        smaple()
        _binding?.loginBtnGoHome?.setOnSingleClickListener {
            (requireActivity() as ToFlowNavigatable).navigateToFlow(NavigationFlow.HomeFlow("test"))
        }
        observeToast()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun observeLoading(){
        viewModel.isLoading.observe(viewLifecycleOwner, {
            Log.e(TAG, "observeLoading: ${it}", )
            (requireActivity() as ShowLoading).showLoading(it)
        })
    }

    fun smaple(){
        viewModel.sample.observe(viewLifecycleOwner,{
            showToast("${it.body} , ${it.statusCode} , ${it.lsg}")
        })
    }
}