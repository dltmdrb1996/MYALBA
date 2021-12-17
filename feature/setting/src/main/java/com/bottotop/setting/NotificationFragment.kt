package com.bottotop.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import com.bottotop.setting.databinding.FragmentNotificationBinding

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding, NotificationViewModel>(R.layout.fragment_notification, "노티_프래그먼트") {

    private val vm by viewModels<NotificationViewModel>()
    override val viewModel get() = vm
    private val notificationAdapter : NotificationAdapter by lazy { NotificationAdapter(viewModel) }

    override fun setBindings() {
        _binding?.viewModel = viewModel
        _binding?.adapter = notificationAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initClick()
    }

    override fun initObserver() {
        viewModel.noti.observe(viewLifecycleOwner,{
            notificationAdapter.submitList(it)
        })
    }

    override fun initClick() {
    }

}