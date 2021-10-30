package com.bottotop.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.home.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.activityViewModels
import com.bottotop.core.global.SharedViewModel
import com.bottotop.model.ScheduleInfo


@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home, "홈_프래그먼트") {

    lateinit var navView: BottomNavigationView
    lateinit var toolbar : Toolbar
    private val vm by viewModels<HomeViewModel>()
    override val viewModel get() = vm
    private val adapter : TodayWorkAdapter by lazy { TodayWorkAdapter(viewModel) }
    private val mainViewModel : SharedViewModel by activityViewModels()

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
        observeLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun setBtn(){
        _binding?.apply {
            btnKakaoLogout.setOnClickListener {
//                this@HomeFragment.viewModel.logoutKakao()
                mainViewModel.test("test")
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
        val dummy = listOf(
            ScheduleInfo("29" , "08","16","8") ,
            ScheduleInfo("29" , "08","16","8") ,
            ScheduleInfo("29" , "08","16","8") ,
            )
        adapter.submitList(dummy)
    }
}