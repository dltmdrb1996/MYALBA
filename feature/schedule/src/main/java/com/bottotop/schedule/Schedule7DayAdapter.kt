package com.bottotop.schedule

import android.content.Context
import android.graphics.Color
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aryanmo.utils.utils.getResources
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.Navigator
import com.bottotop.core.util.DateTime
import com.bottotop.model.Schedule
import com.bottotop.schedule.databinding.ItemBinding
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import kotlin.collections.ArrayList

class Schedule7DayAdapter(private val viewModel: ScheduleViewModel) :
    RecyclerView.Adapter<Schedule7DayAdapter.AdapterViewHolder>() {

    private val items: ArrayList<Schedule> = arrayListOf()
    private val dateUtil = DateTime()
    private val week = dateUtil.getTodayWeek()
    private val month = dateUtil.currentMonth

    fun addHeaderAndSumbitList(list: List<Schedule>) {
        val diffCallback = DiffUtilCallback(items, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    class AdapterViewHolder constructor(
        val binding: ItemBinding, val viewModel: ScheduleViewModel,
        val today: String, val month: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        private val adapter : DayAdapter by lazy { DayAdapter(viewModel) }
        fun bind(item: Schedule) {
            binding.apply {
                val week = item.day.takeLast(3)
                if (item.itemMonth != item.currentMonth) this.itemCard.alpha = 0.5f
                if (week == "토요일" || week == "일요일") {
                    title.setTextColor(Color.rgb(186, 7, 9))
                }
                if (item.day == today && item.currentMonth == month) {
                    this.itemCard.strokeWidth = 8
                } else {
                    this.itemCard.strokeWidth = 0
                }
                schedule = item
                viewModel = this@AdapterViewHolder.viewModel
                adapter = this@AdapterViewHolder.adapter
                cardBtn.setOnClickListener {
                    it.findNavController().navigate(R.id.action_scheduleFragment_to_scheduleDetailFragment)
                }
                this@AdapterViewHolder.adapter.submitList(item.text)
                executePendingBindings()
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                viewModel: ScheduleViewModel,
                today: String,
                month: Int,
            ): AdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBinding.inflate(layoutInflater, parent, false)
                return AdapterViewHolder(binding, viewModel, today, month)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int): AdapterViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return AdapterViewHolder.from(
            parent,
            viewModel,
            week,
            month
        )
    }


    override fun getItemCount(): Int {
        return return if (items.size > 0) {
            200
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val item = items[position % items.count()]
        holder.bind(item)
    }


}

private class DiffUtilCallback(
    private val oldItems: List<Schedule>,
    private val newItems: List<Schedule>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int =
        oldItems.size

    override fun getNewListSize(): Int =
        newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.currentMonth == newItem.currentMonth
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }

}

