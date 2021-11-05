package com.bottotop.schedule

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bottotop.core.util.DateTime
import com.bottotop.model.ScheduleItem
import com.bottotop.schedule.databinding.ItemBinding
import java.lang.StringBuilder
import kotlin.math.log

class Schedule7DayAdapter(
    private val viewModel: ScheduleViewModel,
    list: List<List<String>>,
    scheduleItem: List<ScheduleItem>
    ) : RecyclerView.Adapter<Schedule7DayAdapter.AdapterViewHolder>() {

    private val items = list
    private val scheduleList = scheduleItem
    private val dateUtil = DateTime()
    private val today = if(dateUtil.getToday().length==1) "0"+dateUtil.getToday() else dateUtil.getToday()
    private val month = if(dateUtil.currentMonth<=9) "0"+dateUtil.currentMonth.toString() else dateUtil.currentMonth.toString()

    class AdapterViewHolder constructor(
        val binding: ItemBinding, val viewModel: ScheduleViewModel,
        val today: String, val month: String
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: List<String>, scheduleList: List<ScheduleItem>) {
            binding.apply {
                title.text = "${item[1]}일 ${item[2]}"

                if(item[2]=="일요일" || item[2] == "토요일") title.setTextColor(Color.rgb(186, 7, 9))

                if(item[0] != item[3]) this.itemCard.alpha = 0.5f

                if (item[1] == today && item[0] == month) this.itemCard.strokeWidth = 8
                else this.itemCard.strokeWidth = 0

                var str  = StringBuilder()
                scheduleList.forEachIndexed { idx , it ->
                    val start = if(it.start.length==1) "0${it.start}" else it.start
                    val end = if(it.end.length==1) "0${it.end}" else it.end
                    if(it.workDay.contains(item[2])) str.append("${it.name} : ${start}~${end}\n")
                }
                content.text = str
                executePendingBindings()
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                viewModel: ScheduleViewModel,
                today: String,
                month: String,
            ): AdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBinding.inflate(layoutInflater, parent, false)
                return AdapterViewHolder(binding, viewModel, today, month)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int): AdapterViewHolder {
        return AdapterViewHolder.from(
            parent,
            viewModel,
            today,
            month
        )
    }


    override fun getItemCount(): Int {
        return if (items.isNotEmpty()) 200 else 0
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val item = items[position % items.count()]
        holder.bind(item,scheduleList)
    }
}

