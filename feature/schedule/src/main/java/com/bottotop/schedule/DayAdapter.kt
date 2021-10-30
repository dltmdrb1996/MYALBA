package com.bottotop.schedule

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottotop.model.ScheduleInfo
import com.bottotop.schedule.databinding.DayScheduleHolderBinding


class DayAdapter(private val viewModel: ScheduleViewModel) :
    ListAdapter<ScheduleInfo, DayAdapter.ViewHolder>(TaskDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel , item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: DayScheduleHolderBinding ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ScheduleViewModel, item: ScheduleInfo ) {
            binding.apply {
                this.item = item
                this.viewModel = viewModel
//                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DayScheduleHolderBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

}

class TaskDiffCallback : DiffUtil.ItemCallback<ScheduleInfo>() {
    override fun areItemsTheSame(oldItem: ScheduleInfo, newItem: ScheduleInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ScheduleInfo, newItem: ScheduleInfo): Boolean {
        return oldItem == newItem
    }
}


