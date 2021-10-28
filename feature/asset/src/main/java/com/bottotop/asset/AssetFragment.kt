package com.bottotop.asset

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.asset.databinding.FragmentAssetBinding
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.setOnSingleClickListener
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigatable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AssetFragment :
    BaseFragment<FragmentAssetBinding, AssetViewModel>(R.layout.fragment_asset, "자원_프래그먼트") {

    private val vm by viewModels<AssetViewModel>()
    override val viewModel get() = vm

    override fun setBindings() {
        _binding?.viewModel = viewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.loginBtnGoHome?.setOnSingleClickListener {
            (requireActivity() as ToFlowNavigatable).navigateToFlow(NavigationFlow.HomeFlow("test"))
        }
        observeLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun observeLoading(){
        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })
    }
}