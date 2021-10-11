package com.bottotop.home

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.home.databinding.FragmentHomeBinding
import com.bottotop.model.UserInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import com.bottotop.core.ext.waitForMeasure


@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home, "홈_프래그먼트") {

    lateinit var navView: BottomNavigationView
    lateinit var toolbar : Toolbar
    private val vm by viewModels<HomeViewModel>()
    override val viewModel get() = vm
    private val adapter : TodayWorkAdapter by lazy { TodayWorkAdapter(viewModel) }

    override fun setBindings() {
        _binding?.viewModel = viewModel
        _binding?.adapter = this.adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBtn()
        setUserInfo()
        todayWorkAdapter()
        observeToast()
        observeLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun setBtn(){
        _binding?.apply {
            btnKakaoLogout.setOnClickListener {
                this@HomeFragment.viewModel.logoutKakao()
            }
            btnNaverLogout.setOnClickListener {
                this@HomeFragment.viewModel.logoutNaver()
            }
        }
    }

    fun setUserInfo(){
        viewModel.info.observe(viewLifecycleOwner,{
        })
    }

    fun observeLoading(){
        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })
    }

    fun todayWorkAdapter(){
        val dummy = listOf(UserInfo.scheduleInfo , UserInfo.scheduleInfo ,UserInfo.scheduleInfo)
        adapter.submitList(dummy)
    }
}