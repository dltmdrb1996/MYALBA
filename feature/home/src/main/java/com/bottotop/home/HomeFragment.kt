package com.bottotop.home

import android.Manifest
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.findNavController
import com.bottotop.core.global.SharedViewModel
import com.bottotop.core.navigation.DeepLinkDestination
import com.bottotop.core.navigation.deepLinkNavigateTo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import android.net.wifi.SupplicantState
import android.content.Context.WIFI_SERVICE
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home, "홈_프래그먼트") {

    private val vm by viewModels<HomeViewModel>()
    override val viewModel get() = vm
    private val todayAdapter: TodayWorkAdapter by lazy { TodayWorkAdapter(viewModel) }
    private val mainViewModel: SharedViewModel by activityViewModels()
    private lateinit var bottomSheet: QrDialog

    override fun setBindings() {
        _binding?.viewModel = viewModel
        _binding?.todayAdapter = this.todayAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheet = QrDialog()
        initObserver()
        initClick()

    }


    override fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })

        viewModel.scheduleItem.observe(viewLifecycleOwner, { list ->
            todayAdapter.submitList(list)
        })

    }

    override fun initClick() {
        binding.tvContent.setOnClickListener {
            val json = Json.encodeToString(viewModel.community.value)
            findNavController().deepLinkNavigateTo(DeepLinkDestination.CommunityDetail(json))
        }

        binding.btnQr.setOnClickListener {
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

    }
}