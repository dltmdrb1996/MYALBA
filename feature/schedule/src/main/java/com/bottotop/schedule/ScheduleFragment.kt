package com.bottotop.schedule

import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.isInvisible
import androidx.core.view.size
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.*
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.util.DateTime
import com.bottotop.schedule.databinding.FragmentScheduleBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.properties.Delegates
import android.view.WindowInsets

import android.view.WindowMetrics

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import androidx.annotation.NonNull


@AndroidEntryPoint
class ScheduleFragment :
    BaseFragment<FragmentScheduleBinding, ScheduleViewModel>(
        R.layout.fragment_schedule,
        "일정_프래그먼트"
    ) {

    private val vm by viewModels<ScheduleViewModel>()
    override val viewModel get() = vm
    override fun setBindings() {
        _binding?.viewModel = viewModel
    }

    var month by Delegates.notNull<Int>()
    private val dateUtil: DateTime = DateTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        month = dateUtil.currentMonth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick()
        initObserver()
    }

    override fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })

        viewModel.viewPager.observe(viewLifecycleOwner, {
            initViewPager(it)
        })
    }

    override fun initClick() {
    }

    private fun initViewPager(schedule: Map<Int, List<List<String>>>?) {
        _binding?.apply {
            val list = listOf<ViewPager2>(viewPager1, viewPager2, viewPager3, viewPager4, viewPager5)
            list.forEachIndexed { index, viewPager ->
                val adapter: Schedule7DayAdapter by lazy { Schedule7DayAdapter(
                    this@ScheduleFragment.viewModel,
                    schedule?.get(index)!!,
                    this@ScheduleFragment.viewModel.scheduleItem.value!!
                ) }
                viewPager.adapter = adapter
                val width = getScreenWidth(requireActivity())
                val paddingToSet = width / 3
                viewPager.setShowSideItems(0, paddingToSet)
                setLocationViewPager(viewPager, schedule?.get(index))
            }
        }
    }

    private fun ViewPager2.setShowSideItems(pageMarginPx: Int, offsetPx: Int) {
        clipToPadding = false
        clipChildren = false
        offscreenPageLimit = 3
        setPageTransformer { page, position ->
            val offset = position * -(2 * offsetPx + pageMarginPx)
            if (this.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -offset
                } else {
                    page.translationX = offset
                }
            } else {
                page.translationY = offset
            }
        }
    }

    // 현재 날짜에맞게 포커스를 맞춰준다.
    private fun setLocationViewPager(
        viewPager: ViewPager2,
        schedule: List<List<String>>?,
    ) {
        val today =
            if (dateUtil.getToday().length == 1) "0" + dateUtil.getToday() else dateUtil.getToday()
        val currentMonth =
            if (dateUtil.currentMonth <= 9) "0" + dateUtil.currentMonth.toString() else dateUtil.currentMonth.toString()
        schedule?.forEach {
            if (it[1] == today && it[0] == currentMonth.toString()) {
                val day = today.take(2).toInt()
                val start =
                    DateTime().getDayTimeToString(dateUtil.getWeekStartDate(day, month))?.toInt()
                val focus = day - start!!
                viewPager.setCurrentItem(97 + focus!!, false)
                viewPager.requestFocus(View.FOCUS_LEFT)
                viewPager.waitForMeasure { view, width, height, x, y ->
                    _binding?.scheduleScroll?.scrollTo(0, y.toInt()+(height*2))
                }
                return
            }
            viewPager.setCurrentItem(98, false)
        }
    }
}