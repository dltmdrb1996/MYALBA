package com.bottotop.member.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bottotop.core.base.BaseFragment
import com.bottotop.member.R
import com.bottotop.member.databinding.FragmentMemberDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MemberDetailFragment() :
    BaseFragment<FragmentMemberDetailBinding, MemberDetailViewModel>(R.layout.fragment_member_detail, "맴버디테일_프래그먼트") {

    private val vm by viewModels<MemberDetailViewModel>()
    override val viewModel get() = vm

    val args: MemberDetailFragmentArgs by navArgs() //Args 만든 후

    override fun setBindings() {
        _binding?.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: ${args.msg}", )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}