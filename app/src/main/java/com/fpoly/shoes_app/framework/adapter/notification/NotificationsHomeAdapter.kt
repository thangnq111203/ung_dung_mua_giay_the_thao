package com.fpoly.shoes_app.framework.adapter.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.domain.model.profile.notification.NotificationsHome

sealed class NotificationsItem {
    data class Header(val title: String) : NotificationsItem()
    data class NotificationItem(val notification: NotificationsHome) : NotificationsItem()
}

class NotificationsHomeAdapter : ListAdapter<NotificationsItem, RecyclerView.ViewHolder>(NotificationsDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerText: TextView = view.findViewById(R.id.headerText)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tittleNotification)
        val body: TextView = view.findViewById(R.id.contentNotification)
        val imageView: ImageView = view.findViewById(R.id.appCompatImageView)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NotificationsItem.Header -> VIEW_TYPE_HEADER
            is NotificationsItem.NotificationItem -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notifications_home, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_HEADER -> {
                val header = getItem(position) as NotificationsItem.Header
                (holder as HeaderViewHolder).headerText.text = header.title
            }
            VIEW_TYPE_ITEM -> {
                val notificationItem = getItem(position) as NotificationsItem.NotificationItem
                val notification = notificationItem.notification
                val itemViewHolder = holder as ItemViewHolder
                itemViewHolder.title.text = notification.title
                itemViewHolder.body.text = notification.body
                // Load image if URL is not null
                notification.image?.let {
                    Glide.with(holder.itemView.context)
                        .load(it)
                        .placeholder(R.drawable.baseline_notifications_active_24)
                        .into(itemViewHolder.imageView)
                } ?: itemViewHolder.imageView.setImageResource(R.drawable.baseline_notifications_active_24)
            }
        }
    }

    // Method to append new data for load more functionality
    @SuppressLint("NotifyDataSetChanged")
    fun appendData(newData: List<NotificationsItem>) {
        val currentList = currentList.toMutableList()

        // Avoid adding duplicate items
        val filteredData = newData.filterNot { newItem ->
            currentList.any { currentItem ->
                areItemsTheSame(currentItem, newItem)
            }
        }

        currentList.addAll(filteredData)
        submitList(currentList)   // Use submitList to update the adapter
    }

    private fun areItemsTheSame(oldItem: NotificationsItem, newItem: NotificationsItem): Boolean {
        return when {
            oldItem is NotificationsItem.Header && newItem is NotificationsItem.Header -> oldItem.title == newItem.title
            oldItem is NotificationsItem.NotificationItem && newItem is NotificationsItem.NotificationItem -> oldItem.notification._id == newItem.notification._id
            else -> false
        }
    }
}

class NotificationsDiffCallback : DiffUtil.ItemCallback<NotificationsItem>() {
    override fun areItemsTheSame(oldItem: NotificationsItem, newItem: NotificationsItem): Boolean {
        return when {
            oldItem is NotificationsItem.Header && newItem is NotificationsItem.Header -> oldItem.title == newItem.title
            oldItem is NotificationsItem.NotificationItem && newItem is NotificationsItem.NotificationItem -> oldItem.notification._id == newItem.notification._id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: NotificationsItem, newItem: NotificationsItem): Boolean {
        return oldItem == newItem
    }
}
