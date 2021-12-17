package com.bottotop.setting

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.core.base.BaseFragment
import com.bottotop.setting.databinding.FragmentInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import android.content.DialogInterface
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigation

@AndroidEntryPoint
class InfoFragment :
    BaseFragment<FragmentInfoBinding, InfoViewModel>(R.layout.fragment_info, "마이페이지_프래그먼트") {

    private val vm by viewModels<InfoViewModel>()
    override val viewModel get() = vm

    override fun setBindings() {
        _binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initClick()
    }

    override fun initObserver() {
        viewModel.navLogin.observe(viewLifecycleOwner,{
            (requireActivity() as ToFlowNavigation).navigateToFlow(NavigationFlow.LoginFlow("back"))
        })
    }

    override fun initClick() {
        unRegister()
    }

    private fun unRegister(){
        binding.btnUnRegister.setOnClickListener {
            val dialog = UnRegisterDialog(viewModel)
            dialog.show(childFragmentManager,dialog.tag)
        }
    }


}