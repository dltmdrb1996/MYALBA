package com.bottotop.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottotop.core.navigation.DeepLinkDestination
import com.bottotop.core.navigation.deepLinkNavigateTo
import com.bottotop.core.util.DateTime
import com.bottotop.model.Notification
import com.bottotop.setting.databinding.NotificationItemBinding

class NotificationAdapter(private val viewModel: NotificationViewModel) :
    ListAdapter<Notification, NotificationAdapter.ViewHolder>(TaskDiffCallback()) {

    private val dataUtil = DateTime()
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val time = dataUtil.getTimeLongToMMDD(item.time)
        holder.bind(item , time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Notification , time : String) {
            binding.item = item
            binding.ivTag.setImageResource(R.drawable.ic_baseline_notifications_24)
            binding.tvTime.text = time
            binding.viewHolder.setOnClickListener { it.findNavController().deepLinkNavigateTo(
                DeepLinkDestination.Community("back"))
            }

            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NotificationItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Notification>() {

    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.content == newItem.content
    }
}

