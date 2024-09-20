package com.fpoly.shoes_app.framework.adapter.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.domain.model.profile.notification.Notification
import com.google.android.material.materialswitch.MaterialSwitch

class NotificationAdapter (
    private val notifications: List<Notification>,
    private val onSwitchChanged: (Notification, Boolean) -> Unit
    ) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

        class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
            @SuppressLint("UseSwitchCompatOrMaterialCode")
            val switchControl: MaterialSwitch = view.findViewById(R.id.switchControl)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_general_setting, parent, false)
            return NotificationViewHolder(view)
        }

        override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
            val notification = notifications[position]
            holder.textViewTitle.text = notification.title
            holder.switchControl.isChecked = notification.isChecked

            holder.switchControl.setOnCheckedChangeListener { _, isChecked ->
                onSwitchChanged(notification, isChecked)
            }
        }

        override fun getItemCount(): Int {
            return notifications.size
        }
}