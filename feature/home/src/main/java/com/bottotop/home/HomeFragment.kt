package com.bottotop.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bottotop.core.ext.isInvisible
import com.bottotop.core.global.SharedViewModel
import com.bottotop.core.navigation.DeepLinkDestination
import com.bottotop.core.navigation.deepLinkNavigateTo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home, "홈_프래그먼트") {

    private val vm by viewModels<HomeViewModel>()
    override val viewModel get() = vm
    private val todayAdapter : TodayWorkAdapter by lazy { TodayWorkAdapter(viewModel) }
    private val mainViewModel : SharedViewModel by activityViewModels()

    override fun setBindings() {
        _binding?.viewModel = viewModel
        _binding?.todayAdapter = this.todayAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTodayWorkAdapter()
        observeLoading()
        initMasterPage()
        initClickEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


    private fun setTodayWorkAdapter(){
        viewModel.scheduleItem.observe(viewLifecycleOwner,{ list ->
            todayAdapter.submitList(list)
        })
    }

    private fun observeLoading(){
        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })
    }

    private fun initClickEvent(){
        binding.tvContent.setOnClickListener {
            val json = Json.encodeToString(viewModel.community.value)
            findNavController().deepLinkNavigateTo(DeepLinkDestination.CommunityDetail(json))
        }
    }

    private fun initMasterPage(){
        viewModel.master.observe(viewLifecycleOwner,{
            if(it){
                binding.workCheck.isInvisible()
                binding.text3.isInvisible()
                binding.textView2.isInvisible()
            }
        })
    }
}