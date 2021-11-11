package com.bottotop.schedule

import android.R.attr
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.*
import com.bottotop.core.navigation.DeepLinkDestination
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigatable
import com.bottotop.core.navigation.deepLinkNavigateTo
import com.bottotop.core.util.DateTime
import com.bottotop.model.Schedule
import com.bottotop.schedule.databinding.FragmentScheduleBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.properties.Delegates
import android.R.attr.button
import android.widget.TextView
import org.w3c.dom.Text


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
        observeLoading()
        setChangeMonthBtn()
        setViewPager()
        observeWorkSchedule()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun observeWorkSchedule(){
        viewModel.scheduleItem.observe(viewLifecycleOwner,{
            viewModel.setViewPagerData(month)
        })
    }

    private fun setViewPager() {
        viewModel.viewPager.observe(viewLifecycleOwner, { schedule ->
            _binding?.apply {
                val tvList = listOf<TextView>(tvPager1, tvPager2, tvPager3, tvPager4, tvPager5)
                val list = listOf<ViewPager2>(viewPager1, viewPager2, viewPager3, viewPager4, viewPager5)
                list.forEachIndexed { index, viewPager ->
                    viewPager.adapter = Schedule7DayAdapter(
                        this@ScheduleFragment.viewModel,
                        schedule[index]!!,
                        this@ScheduleFragment.viewModel.scheduleItem.value!!
                        )
                    viewPager.offscreenPageLimit = 3
                    viewPager.setPageTransformer { page, position ->
                        val offset = position * -(2 * 250 + 250)
                        page.translationX = offset
                    }
                    setLocationViewPager(viewPager, schedule[index], tvList[index])
                }
            }
        })
    }

    // 현재 날짜에맞게 포커스를 맞춰준다.
    private fun setLocationViewPager(
        viewPager: ViewPager2,
        schedule: List<List<String>>?,
        textView: TextView
    ) {
        val today = if(dateUtil.getToday().length==1) "0"+dateUtil.getToday() else dateUtil.getToday()
        val currentMonth = if(dateUtil.currentMonth<=9) "0"+dateUtil.currentMonth.toString() else dateUtil.currentMonth.toString()
        schedule?.forEach {
            if (it[1] == today && it[0] == currentMonth.toString()) {
                val day = today.take(2).toInt()
                val start = DateTime().getDayTimeToString(dateUtil.getWeekStartDate(day, month))?.toInt()
                val focus = day - start!!
                viewPager.setCurrentItem(97 + focus!!, false)
                viewPager.requestFocus(View.FOCUS_LEFT)
                viewPager.waitForMeasure { view, width, height, x, y ->
                    _binding?.scheduleScroll?.scrollTo(0, y.toInt() + 100)
                }
                return
            }
            viewPager.setCurrentItem(98, false)
        }
    }

    fun setChangeMonthBtn() {
        binding.apply {
            schedulePreviousTv.text = (month - 1).toString() + "월"
            scheduleCurrentTv.text = month.toString() + "월"
            scheduleNextTv.text = (month + 1).toString() + "월"
            scheduleBackBtn.setOnSingleClickListener {
                if (month <= 1) return@setOnSingleClickListener
                changeMonthBack()
            }
            schedulePreviousTv.setOnSingleClickListener {
                if (month <= 1) return@setOnSingleClickListener
                changeMonthBack()
            }
            scheduleNextBtn.setOnSingleClickListener {
                if (month >= 12) return@setOnSingleClickListener
                changeMonthPrev()
            }
            scheduleNextTv.setOnSingleClickListener {
                if (month >= 12) return@setOnSingleClickListener
                changeMonthPrev()
            }
        }
    }

    private fun changeMonthBack() {
        binding.apply {
            if (scheduleNextTv.isInvisible) scheduleNextTv.isVisible()
            month = month - 1
            this@ScheduleFragment.viewModel.setViewPagerData(month)
            schedulePreviousTv.text = (month - 1).toString() + "월"
            scheduleCurrentTv.text = month.toString() + "월"
            scheduleNextTv.text = (month + 1).toString() + "월"
            if (month == 1) {
                schedulePreviousTv.isInvisible()
            }
        }
    }

    private fun changeMonthPrev() {
        binding.apply {
            if (schedulePreviousTv.isInvisible) schedulePreviousTv.isVisible()
            month = month + 1
            this@ScheduleFragment.viewModel.setViewPagerData(month)
            schedulePreviousTv.text = (month - 1).toString() + "월"
            scheduleCurrentTv.text = month.toString() + "월"
            scheduleNextTv.text = (month + 1).toString() + "월"
            if (month == 12) {
                if (month == 12) scheduleNextTv.isInvisible()
            }
        }
    }

    fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            (requireActivity() as ShowLoading).showLoading(it)
        })
    }

    fun navigate(flow: NavigationFlow) {
        (requireActivity() as ToFlowNavigatable).navigateToFlow(flow)
    }
}