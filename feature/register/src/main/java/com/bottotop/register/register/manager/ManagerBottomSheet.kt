package com.bottotop.register.register.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bottotop.core.ext.isInvisible
import com.bottotop.core.ext.isVisible
import com.bottotop.core.ext.showToast
import com.bottotop.register.R
import com.bottotop.register.databinding.ManagerBottomSheetBinding
import com.bottotop.register.register.RegisterViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ManagerBottomSheet(private val viewModel: RegisterViewModel) : BottomSheetDialogFragment() {

    private var _binding: ManagerBottomSheetBinding? = null
    private val binding get() = _binding!!

//    private val viewModel : RegisterViewModel by viewModels(
//        ownerProducer = { requireParentFragment() }
//    )

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.manager_bottom_sheet, container, false)
        _binding?.apply {
            lifecycleOwner = this@ManagerBottomSheet
            viewModel = this@ManagerBottomSheet.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver(){

        viewModel.pay.observe(viewLifecycleOwner,{
            viewModel.isLoading.observe(viewLifecycleOwner,{
                showLoading(it)
            })
            if(it.isNullOrEmpty()){
                binding.tvPayNotice.isInvisible()
                binding.btnRegCom.isEnabled = true
                return@observe
            }
            if(it.first()=='0') {
                binding.tvPayNotice.isVisible()
                binding.btnRegCom.isEnabled = false
            }
            else binding.tvPayNotice.isInvisible()
        })

        viewModel.com_tel.observe(viewLifecycleOwner,{
            if(it.isNullOrEmpty()){
                binding.tvTelNotice.isInvisible()
                return@observe
            }
            if(it.length>=12) binding.tvTelNotice.isVisible()
            else binding.tvTelNotice.isInvisible()
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) binding.nestedScrollView.alpha = 0.5f
        else binding.nestedScrollView.alpha = 1f
        binding.nestedScrollView.isClickable = isLoading
        binding.loadingView.isInProgress = isLoading
    }
}