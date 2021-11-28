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
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("회원탈퇴").setMessage("이 앱에 관련된 모든 데이터가 삭제됩니다.")
            builder.setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, id ->
                    viewModel.unRegister()
                })

            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->

                })

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }


}