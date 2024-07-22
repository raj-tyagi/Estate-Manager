package com.example.databaseapp

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.databaseapp.databinding.ItemLogBinding

class LogAdapter : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    private var logs = listOf<Log>()

    fun submitList(list: List<Log>) {
        logs = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = ItemLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logs[position]
        holder.bind(log)
    }

    override fun getItemCount(): Int = logs.size

    inner class LogViewHolder(private val binding: ItemLogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(log: Log) {
            binding.logTextView.text = log.message
            binding.timestampTextView.text = log.timestamp
            binding.newOwnerTextView.text = log.newOwner
            binding.oldOwnerTextView.text = log.oldOwner
            binding.dealAmountTextView.text = log.dealAmount
            binding.propertySizeTextView.text = log.propertySize
            binding.dealerAgentTextView.text = log.dealerAgent
            binding.commissionEarnedTextView.text = log.commissionEarned
            binding.propertyTypeTextView.text = log.propertyType
            binding.propertyAddressTextView.text = log.propertyAddress

            // Set imageUrl as clickable link
            binding.imageUrlTextView.text = log.imageUrl
            binding.imageUrlTextView.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}
