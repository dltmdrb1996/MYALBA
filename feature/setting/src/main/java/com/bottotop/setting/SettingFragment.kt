package com.bottotop.setting

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.viewModels
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.withDelayOnMain
import com.bottotop.core.global.PreferenceHelper
import com.bottotop.core.global.PreferenceHelper.get
import com.bottotop.core.global.PreferenceHelper.set
import com.bottotop.setting.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import timber.log.Timber
import android.preference.PreferenceManager

import android.content.SharedPreferences


@AndroidEntryPoint
class SettingFragment :
    BaseFragment<FragmentSettingBinding, SettingViewModel>(R.layout.fragment_setting, "셋팅_프래그먼트") {

    private val vm by viewModels<SettingViewModel>()
    override val viewModel get() = vm
    lateinit var mPref: SharedPreferences

    override fun setBindings() {
        _binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPref = PreferenceHelper.defaultPrefs(requireActivity().applicationContext)
        init()
        initClick()
        initObserver()
    }

    override fun initObserver() {}

    override fun initClick() {
//        setFCM()
        setDarkMode()
    }

    private fun init() {
        val dark = mPref["dark", false]
        val fcm = mPref["fcm", true]
        binding.toggleDarkMode.isChecked = dark
        binding.toggleFcm.isChecked = fcm
    }


    fun setDarkMode(){
        binding.toggleDarkMode.setOnCheckedChangeListener { compoundButton, ischecked ->
            if(ischecked){
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                mPref["dark"] = true
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                mPref["dark"] = false
            }
        }
    }

//    private fun setFCM() {
//        binding.toggleFcm.setOnCheckedChangeListener { compoundButton, ischecked ->
//            if(ischecked){
//                viewModel.fcmOn()
//                mPref["fcm"] = true
//            } else {
//                viewModel.fcmOff()
//                mPref["fcm"] = false
//            }
//        }
//    }
}