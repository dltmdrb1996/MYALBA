package com.bottotop.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.register.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment :
    BaseFragment<FragmentRegisterBinding, RegisterViewModel>(R.layout.fragment_register, "가입_프래그먼트") {

    private val vm by viewModels<RegisterViewModel>()
    override val viewModel get() = vm

    override fun setBindings() {
        _binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToast()
        observeLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun observeLoading(){
        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })
    }
}