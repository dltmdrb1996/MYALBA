package com.bottotop.member

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.member.databinding.FragmentMemberBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberFragment :
    BaseFragment<FragmentMemberBinding, MemberViewModel>(R.layout.fragment_member, "맴버_프래그먼트") {

    private val vm by viewModels<MemberViewModel>()
    override val viewModel get() = vm

    private val adapter : MemberAdapter by lazy { MemberAdapter(viewModel ,requireContext()) }
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

        viewModel.members.observe(viewLifecycleOwner,{
            adapter.submitList(it)
        })

    }

    override fun initClick() {}

}