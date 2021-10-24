package com.bottotop.myalba

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.bottotop.core.ext.isVisible
import com.bottotop.myalba.databinding.FragmentMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.lang.NullPointerException


@AndroidEntryPoint
class MainFragment : Fragment() {
    lateinit var navView: BottomNavigationView
    lateinit var toolbar : Toolbar
    lateinit var appBarLayout: AppBarLayout
    private var _binding: FragmentMainBinding? = null
    private lateinit var mViewPager: ViewPager2
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        toolbar = (requireActivity() as MainActivity).binding.appToolbar
        navView = (requireActivity() as MainActivity).binding.navViewFragment
        appBarLayout = (requireActivity() as MainActivity).binding.ToolbarLayout
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        toolbar.isVisible()
        navView.isVisible()
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPager()
        setBottomNav()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView: 종료")
        _binding = null
    }


    private inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            navView.selectedItemId = when (position % 5) {
                0 -> {
                    toolbar.title = "직원관리"
                    R.id.member_flow
                }
                1 -> {
                    toolbar.title = "커뮤니티"
                    R.id.community_flow
                }

                2 -> {
                    toolbar.title = "내알빠"
                    R.id.home_flow
                }
                3 -> {
                    toolbar.title = "일정관리"
                    R.id.schedule_flow
                }
                4 -> {
                    toolbar.title = "임금관리"
                    R.id.asset_flow
                }
                else -> Log.e(TAG, "onPageSelected: 없는포지션")
            }
        }
    }

    fun setBottomNav() {
        navView.setOnItemSelectedListener {
            val current = mViewPager.currentItem%5
            when (it.itemId) {
                R.id.member_flow -> {
                    clickBottomNav(0, current)
                    return@setOnItemSelectedListener true
                }
                R.id.community_flow -> {
                    clickBottomNav(1, current)
                    return@setOnItemSelectedListener true
                }
                R.id.home_flow -> {
                    clickBottomNav(2, current)
                    return@setOnItemSelectedListener true
                }
                R.id.schedule_flow -> {
                    clickBottomNav(3, current)
                    return@setOnItemSelectedListener true
                }
                R.id.asset_flow -> {
                    clickBottomNav(4, current)
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener true
            }
        }
    }

    fun setViewPager() {
        val firstFragmentStateAdapter = FragmentStateAdapter(this)
        mViewPager = _binding?.viewPager!!
        mViewPager.adapter = firstFragmentStateAdapter
        disableCacheOfViewPager()
        mViewPager.offscreenPageLimit = 1
        mViewPager.registerOnPageChangeCallback(PageChangeCallback())
        mViewPager.setCurrentItem(102, false)
        mViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                appBarLayout.setExpanded(true)
            }
        })
    }

    // 클릭시 현제 뷰페이저 기준으로 가까운 방향으로 뷰페이저를 이동시켜준다.
    fun clickBottomNav(num: Int, current: Int) {
        navView.menu.getItem(num).isChecked = true
        var back = 0
        var front = 0
        if (current > num) {
            front = 5 + num - current
            back = current - num
        } else if (current < num) {
            front = num - current
            back = 5 - num + current
        }
        if (back == front) {
            return
        } else if (back > front) {
            mViewPager.setCurrentItem(mViewPager.currentItem+front , true)
        } else {
            mViewPager.setCurrentItem(mViewPager.currentItem-back , true)
        }
    }

    // viewPager2 offScreen 에러 해결 코드
    private fun disableCacheOfViewPager() {
        try {
            (_binding?.viewPager?.getChildAt(0) as RecyclerView).layoutManager!!.isItemPrefetchEnabled = false
            (_binding?.viewPager?.getChildAt(0) as RecyclerView).setItemViewCacheSize(0)
        } catch (e: NullPointerException) {
            Log.i(TAG, "disableCacheOfViewPager: " + e.message)
        }
    }

    companion object {
        private val TAG = "몌인프럐그먼트"
    }

}