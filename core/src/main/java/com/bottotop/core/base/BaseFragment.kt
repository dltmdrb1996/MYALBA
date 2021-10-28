package com.bottotop.core.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.bottotop.core.ext.showToast
import com.bottotop.core.model.EventObserver
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

abstract class BaseFragment<B : ViewDataBinding , VM : BaseViewModel>(@LayoutRes private val layoutResId: Int , name : String) : Fragment() {

    protected var _binding: B? = null
    protected val binding get() = _binding!!
    protected abstract val viewModel : VM
    protected val TAG = name

    protected abstract fun setBindings()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        performDataBinding()
        observeToast()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView: 종료", )
        _binding = null
    }

    private fun performDataBinding() {
        _binding?.lifecycleOwner = this
        setBindings()
    }

    open fun observeToast(){
        viewModel.toast.observe(viewLifecycleOwner, EventObserver{
            showToast(it,false)
        })
    }

}