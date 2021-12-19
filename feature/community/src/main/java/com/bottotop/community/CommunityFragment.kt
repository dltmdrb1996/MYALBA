package com.bottotop.community

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.community.databinding.FragmentCommunityBinding
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CommunityFragment :
    BaseFragment<FragmentCommunityBinding, CommunityViewModel>(R.layout.fragment_community, "커뮤니티_프래그먼트") {

    private val vm by viewModels<CommunityViewModel>()
    override val viewModel get() = vm
    private val adapter : CommunityAdapter by lazy { CommunityAdapter(viewModel) }
    private lateinit var bottomSheet : CreateBottomSheet
    override fun setBindings() {
        _binding?.viewModel = viewModel
        _binding?.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheet = CreateBottomSheet(viewModel)
        initObserver()
        initClick()
    }

    override fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })

        viewModel.success.observe(viewLifecycleOwner,{
            binding.recyclerView.smoothScrollToPosition(0)
        })

        viewModel.communityList.observe(viewLifecycleOwner,{
            adapter.submitList(it)
        })
    }

    override fun initClick() {
        binding.apply {
            communityBtnCreate.setOnClickListener {
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        }
    }
}