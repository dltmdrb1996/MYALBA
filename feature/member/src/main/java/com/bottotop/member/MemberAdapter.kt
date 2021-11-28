package com.bottotop.member

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottotop.core.ext.setOnSingleClickListener
import com.bottotop.core.navigation.DeepLinkDestination
import com.bottotop.core.navigation.deepLinkNavigateTo
import com.bottotop.member.databinding.ViewholderMemberBinding

class MemberAdapter (private val viewModel: MemberViewModel , private val context: Context) :
    ListAdapter<MemberModel, MemberAdapter.ViewHolder>(TaskDiffCallback()) {



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item , context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ViewholderMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: MemberViewModel, item: MemberModel , context: Context) {
            if (item.workOn == "on") binding.tvWorkOn.text = "출근중"
            binding.item = item
            binding.viewModel = viewModel
            if (item.position == "A") binding.tvPay.text = ""

            binding.holderLayout.setOnSingleClickListener {
                it.findNavController().deepLinkNavigateTo(DeepLinkDestination.MemberDetail(item.id))
            }
            binding.btnCall.setOnClickListener {
                context.startActivity(Intent(ACTION_DIAL, Uri.parse("tel:${item.tel}")))
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ViewholderMemberBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<MemberModel>() {
    override fun areItemsTheSame(oldItem: MemberModel, newItem: MemberModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MemberModel, newItem: MemberModel): Boolean {
        return oldItem == newItem
    }
}