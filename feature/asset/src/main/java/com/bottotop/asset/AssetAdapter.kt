package com.bottotop.asset

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottotop.asset.databinding.ViewholderAssetBinding
import com.bottotop.core.ext.setOnSingleClickListener
import com.bottotop.core.navigation.DeepLinkDestination
import com.bottotop.core.navigation.deepLinkNavigateTo
import com.bottotop.core.util.DateTime
import com.bottotop.model.Community
import com.bottotop.model.Schedule


class AssetAdapter(private val viewModel: AssetViewModel) :
    ListAdapter<Pair<String,List<Pair<String,Schedule>>>, AssetAdapter.ViewHolder>(TaskDiffCallback()) {

    val month = DateTime().getYearMonth()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item, month)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: ViewholderAssetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: AssetViewModel, item: Pair<String,List<Pair<String,Schedule>>> , month : String) {
            binding.viewModel = viewModel
            var money = 0
            var monthMoney = 0
            var currentMonth = false
            val userId = item.second[0].second.id
            item.second.forEach {
                if(it.second.month==month) currentMonth =true
                it.second.scheduleInfo.forEach {
                    if(it.content.workPay=="null") return@forEach
                    if(currentMonth) monthMoney += it.content.workPay.toInt()
                    money += it.content.workPay.toInt()
                }

                currentMonth=false
            }

            binding.apply {
                tvName.text = item.first
                tvMoney.text = money.toString()
                tvMonthMoney.text = monthMoney.toString()
                viewHolder.setOnSingleClickListener {
                    it.findNavController().deepLinkNavigateTo(DeepLinkDestination.MemberDetail(userId))
                }
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ViewholderAssetBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Pair<String,List<Pair<String,Schedule>>>>() {

    override fun areItemsTheSame(
        oldItem: Pair<String, List<Pair<String,Schedule>>>,
        newItem: Pair<String, List<Pair<String,Schedule>>>
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Pair<String, List<Pair<String,Schedule>>>,
        newItem: Pair<String, List<Pair<String,Schedule>>>
    ): Boolean {
        return oldItem == newItem
    }
}

