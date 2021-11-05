package com.bottotop.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bottotop.community.databinding.CreateBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CreateBottomSheet(private val viewModel: CommunityViewModel) : BottomSheetDialogFragment() {

    private var _binding: CreateBottomSheetBinding? = null
    private val binding get() = _binding!!


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
        _binding = DataBindingUtil.inflate(inflater, R.layout.create_bottom_sheet, container, false)
        _binding?.apply {
            lifecycleOwner = this@CreateBottomSheet
            viewModel = this@CreateBottomSheet.viewModel
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
        viewModel.content.observe(viewLifecycleOwner,{
            if(it.isNullOrEmpty()) {
                binding.btnCreate.isEnabled =false
                binding.btnCreate.alpha = 0.3f
            } else {
                binding.btnCreate.isEnabled =true
                binding.btnCreate.alpha = 1f
            }
        })
    }


}