package com.bottotop.community

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.community.databinding.FragmentCommunityBinding
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.setOnSingleClickListener
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigatable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment :
    BaseFragment<FragmentCommunityBinding, CommunityViewModel>(R.layout.fragment_community, "커뮤니티_프래그먼트") {

    private val vm by viewModels<CommunityViewModel>()
    override val viewModel get() = vm

    override fun setBindings() {
        _binding?.viewModel = viewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.loginBtnGoHome?.setOnSingleClickListener {
            (requireActivity() as ToFlowNavigatable).navigateToFlow(NavigationFlow.HomeFlow("test"))
        }
        observeToast()
        observeLoading()
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

}