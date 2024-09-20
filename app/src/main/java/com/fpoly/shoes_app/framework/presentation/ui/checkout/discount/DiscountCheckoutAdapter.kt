package com.fpoly.shoes_app.framework.presentation.ui.checkout.discount

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.ItemDiscountCheckoutBinding
import com.fpoly.shoes_app.framework.domain.model.Discount
import com.fpoly.shoes_app.utility.formatDiscountDescription
import com.fpoly.shoes_app.utility.formatDiscountTitle
import javax.inject.Inject

private val diff = object : DiffUtil.ItemCallback<Pair<Discount, Boolean>>() {
    override fun areItemsTheSame(
        oldItem: Pair<Discount, Boolean>,
        newItem: Pair<Discount, Boolean>
    ) =
        oldItem.first.id == newItem.first.id

    override fun areContentsTheSame(
        oldItem: Pair<Discount, Boolean>,
        newItem: Pair<Discount, Boolean>
    ) =
        oldItem == newItem
}

class DiscountCheckoutAdapter @Inject constructor() :
    ListAdapter<Pair<Discount, Boolean>, DiscountViewHolder>(diff) {

    private lateinit var _onClick: (Discount) -> Unit
    fun setOnClick(onClick: (Discount) -> Unit) {
        _onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DiscountViewHolder(
        ItemDiscountCheckoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), _onClick
    )

    override fun onBindViewHolder(holder: DiscountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiscountViewHolder(
    private val binding: ItemDiscountCheckoutBinding,
    private val onClick: (Discount) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(ship: Pair<Discount, Boolean>) {
        binding.run {
            tvTitle.text = ship.first.discount?.formatDiscountTitle()
            tvDescription.text = ship.first.date?.formatDiscountDescription()
            viewSelected.isVisible = ship.second
            root.setOnClickListener {
                onClick(ship.first)
            }
        }
    }
}
