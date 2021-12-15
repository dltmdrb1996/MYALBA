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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("셋팅프래그먼트 이닛테스트")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPref = PreferenceHelper.defaultPrefs(requireActivity().applicationContext)
        initDarkMod()
        initFCM()
        initClick()
        initObserver()
    }

    override fun initObserver() {}

    override fun initClick() {
        setFCM()
        setDarkMode()
    }

    private fun initDarkMod() {
        val dark = mPref["dark", false]
        val darkModeTab = binding.darkModeTab
        val darkOn = darkModeTab.getTabAt(0)
        val darkOff = darkModeTab.getTabAt(1)
        if (dark) darkOn?.select()
        else darkOff?.select()
    }

    private fun initFCM() {
        val fcm = mPref["fcm", true]
        val fcmTab = binding.fcmTab
        val fcmOn = fcmTab.getTabAt(0)
        val fcmOff = fcmTab.getTabAt(1)
        if (fcm) fcmOn?.select()
        else fcmOff?.select()
    }



    private fun setDarkMode() {
        binding.darkModeTab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.text == "켜기") {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                    mPref["dark"] = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                    mPref["dark"] = false
                }
                initDarkMod()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun setFCM() {
        binding.fcmTab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.text == "켜기") {
                    viewModel.fcmOn()
                    mPref["fcm"] = true
                } else {
                    viewModel.fcmOff()
                    mPref["fcm"] = false
                }
                initFCM()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

}