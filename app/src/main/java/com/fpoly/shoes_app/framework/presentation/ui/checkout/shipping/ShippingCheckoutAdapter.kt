package com.fpoly.shoes_app.framework.presentation.ui.checkout.shipping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.ItemShippingCheckoutBinding
import com.fpoly.shoes_app.framework.domain.model.Ship
import com.fpoly.shoes_app.utility.formatPriceShoe
import javax.inject.Inject

private val diff = object : DiffUtil.ItemCallback<Pair<Ship, Boolean>>() {
    override fun areItemsTheSame(oldItem: Pair<Ship, Boolean>, newItem: Pair<Ship, Boolean>) =
        oldItem.first.id == newItem.first.id

    override fun areContentsTheSame(oldItem: Pair<Ship, Boolean>, newItem: Pair<Ship, Boolean>) =
        oldItem == newItem
}

class ShippingCheckoutAdapter @Inject constructor() :
    ListAdapter<Pair<Ship, Boolean>, ShippingViewHolder>(diff) {

    private lateinit var _onClick: (Ship) -> Unit
    fun setOnClick(onClick: (Ship) -> Unit) {
        _onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ShippingViewHolder(
        ItemShippingCheckoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), _onClick
    )

    override fun onBindViewHolder(holder: ShippingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ShippingViewHolder(
    private val binding: ItemShippingCheckoutBinding,
    private val onClick: (Ship) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(ship: Pair<Ship, Boolean>) {
        binding.run {
            tvTitle.text = ship.first.title
            tvDescription.text = ship.first.description
            tvPrice.text = ship.first.price?.formatPriceShoe()
            viewSelected.isVisible = ship.second

            root.setOnClickListener {
                onClick(ship.first)
            }
        }
    }
}
