package com.bottotop.asset

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.asset.databinding.FragmentAssetBinding
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AssetFragment :
    BaseFragment<FragmentAssetBinding, AssetViewModel>(R.layout.fragment_asset, "자원_프래그먼트") {

    private val vm by viewModels<AssetViewModel>()
    override val viewModel get() = vm
    private val adapter: AssetAdapter by lazy { AssetAdapter(viewModel) }

    override fun setBindings() {
        _binding?.viewModel = viewModel
        _binding?.adapter = adapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initClick()
    }

    override fun initObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })

        viewModel.schedules.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    override fun initClick() {

    }

}