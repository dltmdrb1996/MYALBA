package com.bottotop.community

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.community.databinding.FragmentCommunityBinding
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment :
    BaseFragment<FragmentCommunityBinding, CommunityViewModel>(R.layout.fragment_community, "커뮤니티_프래그먼트") {

    private val vm by viewModels<CommunityViewModel>()
    override val viewModel get() = vm
    private val adapter : CommunityAdapter by lazy { CommunityAdapter(viewModel) }
    lateinit var bottomSheet : CreateBottomSheet
    override fun setBindings() {
        _binding?.viewModel = viewModel
        _binding?.adapter = adapter
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheet = CreateBottomSheet(viewModel)
        initCommunityAdapter()
        observeLoading()
        focusTop()
        initClickEvent()
        viewModel.initCommunity()
    }

    private fun observeLoading(){
        viewModel.isLoading.observe(viewLifecycleOwner, {
            Log.e(TAG, "observeLoading: ${it}", )
            (requireActivity() as ShowLoading).showLoading(it)
        })
    }

    private fun initCommunityAdapter(){
        viewModel.communityList.observe(viewLifecycleOwner,{
            adapter.submitList(it)
        })
    }

    private fun focusTop(){
        viewModel.success.observe(viewLifecycleOwner,{
            binding.recyclerView.smoothScrollToPosition(0)
        })
    }

    private fun initClickEvent(){
        binding.apply {
            communityBtnCreate.setOnClickListener {
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        }
    }

}