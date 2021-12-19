package com.bottotop.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottotop.community.databinding.ViewholderCommunityBinding
import com.bottotop.core.navigation.DeepLinkDestination
import com.bottotop.core.navigation.deepLinkNavigateTo
import com.bottotop.model.Community
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class CommunityAdapter(private val viewModel: CommunityViewModel) :
    ListAdapter<Community, CommunityAdapter.ViewHolder>(TaskDiffCallback()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ViewholderCommunityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: CommunityViewModel, item: Community) {
            binding.item = item
            binding.communityViewHolderTvCommentSize.text = item.comment.size.toString()
            binding.viewModel = viewModel
            val community = Json.encodeToString(item)
            binding.viewHolder.setOnClickListener {
                it.findNavController().deepLinkNavigateTo(DeepLinkDestination.CommunityDetail(community))
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ViewholderCommunityBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Community>() {
    override fun areItemsTheSame(oldItem: Community, newItem: Community): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Community, newItem: Community): Boolean {
        return oldItem.content == newItem.content
    }
}

