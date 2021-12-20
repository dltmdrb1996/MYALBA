package com.bottotop.community

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bottotop.community.databinding.FragmentCommunityBinding
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.showToast
import com.bottotop.core.model.EventObserver
import com.bottotop.core.navigation.DeepLinkDestination
import com.bottotop.core.navigation.deepLinkNavigateTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
        viewModel.initCommunity()
        bottomSheet = CreateBottomSheet(viewModel)
        initObserver()
        initClick()
    }

    override fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })

        viewModel.success.observe(viewLifecycleOwner,EventObserver{
            bottomSheet.dismiss()
            val community = Json.encodeToString(it)
            findNavController().deepLinkNavigateTo(DeepLinkDestination.CommunityDetail(community))
        })

        viewModel.communityList.observe(viewLifecycleOwner,{
            adapter.submitList(it)
        })

        viewModel.failure.observe(viewLifecycleOwner,EventObserver{
            bottomSheet.dismiss()
            showToast("등록에 실패했습니다.")
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