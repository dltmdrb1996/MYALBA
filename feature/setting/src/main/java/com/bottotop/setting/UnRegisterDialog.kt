package com.bottotop.setting

import android.app.AlertDialog
import android.app.Dialog
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.bottotop.setting.databinding.DialogUnregisterBinding


class UnRegisterDialog(val viewModel: InfoViewModel) : DialogFragment() {

    var _binding: DialogUnregisterBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_unregister, container, false)
        _binding?.viewModel =viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }

    }
}