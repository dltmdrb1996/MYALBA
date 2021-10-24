package com.bottotop.core.base

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.icu.lang.UCharacter.GraphemeClusterBreak.V

import androidx.coordinatorlayout.widget.CoordinatorLayout




abstract class BaseBottomSheet<B : ViewDataBinding >(private val layoutId: Int) : BottomSheetDialogFragment() {

    protected var _binding: B? = null
    private val binding get() = _binding!!

    protected abstract fun setBindings()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        performDataBinding()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun performDataBinding() {
        setBindings()
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }


}
