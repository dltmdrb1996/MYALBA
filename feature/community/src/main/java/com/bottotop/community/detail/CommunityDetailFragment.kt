package com.bottotop.community.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bottotop.community.R
import com.bottotop.community.databinding.FragmentCommunityDetailBinding
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.isGone
import com.bottotop.core.ext.isVisible
import dagger.hilt.android.AndroidEntryPoint
import com.bottotop.core.ext.inputMethodManager


@AndroidEntryPoint
class CommunityDetailFragment :
    BaseFragment<FragmentCommunityDetailBinding, CommunityDetailViewModel>(R.layout.fragment_community_detail, "커뮤니티디테일_프래그먼트") {

    private val vm by viewModels<CommunityDetailViewModel>()
    override val viewModel get() = vm

    val args: CommunityDetailFragmentArgs by navArgs() //Args 만든 후
    private val adapter : CommunityDetailAdapter by lazy { CommunityDetailAdapter(viewModel) }

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

        viewModel.content.observe(viewLifecycleOwner,{
            if(it.isNullOrEmpty()) {
                binding.communityBtnCreate.isGone()
            } else {
                binding.communityBtnCreate.isVisible()
            }
        })

        viewModel.comment.observe(viewLifecycleOwner,{
            if(it.isEmpty()){
                binding.emptyImg.isVisible()
            }else{
                binding.emptyImg.isGone()
                Log.e(TAG, "initComment: $it")
                adapter.submitList(it)
            }
        })

    }

    override fun initClick() {
        binding.tvRipple.setOnClickListener {
            binding.edtComment.requestFocus()
            val imm = context?.inputMethodManager
            imm!!.showSoftInput(binding.edtComment, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}