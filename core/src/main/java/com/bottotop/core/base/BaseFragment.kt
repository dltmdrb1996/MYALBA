package com.bottotop.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.bottotop.core.ext.showToast
import com.bottotop.core.model.EventObserver
import timber.log.Timber


abstract class BaseFragment<B : ViewDataBinding , VM : BaseViewModel>(@LayoutRes private val layoutResId: Int , name : String) : Fragment() {

    protected var _binding: B? = null
    protected val binding get() = _binding!!
    protected abstract val viewModel : VM
    protected val TAG = name

    protected abstract fun setBindings()
    protected abstract fun initObserver()
    protected abstract fun initClick()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        performDataBinding()
        observeToast()
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun performDataBinding() {
        _binding?.lifecycleOwner = this
        setBindings()
    }

    open fun observeToast(){
        viewModel.toast.observe(viewLifecycleOwner, EventObserver{
            showToast(it,true)
        })
    }



}