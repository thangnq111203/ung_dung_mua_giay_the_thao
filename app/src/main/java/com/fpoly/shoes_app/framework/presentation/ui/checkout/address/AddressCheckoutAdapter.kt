package com.fpoly.shoes_app.framework.presentation.ui.checkout.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.ItemAddressCheckoutBinding
import com.fpoly.shoes_app.framework.domain.model.profile.address.Addresse
import javax.inject.Inject

private val diff = object : DiffUtil.ItemCallback<Triple<Addresse, Boolean, Boolean>>() {
    override fun areItemsTheSame(
        oldItem: Triple<Addresse, Boolean, Boolean>,
        newItem: Triple<Addresse, Boolean, Boolean>,
    ) =
        oldItem.first.id == newItem.first.id

    override fun areContentsTheSame(
        oldItem: Triple<Addresse, Boolean, Boolean>,
        newItem: Triple<Addresse, Boolean, Boolean>,
    ) =
        oldItem == newItem
}

class AddressCheckoutAdapter @Inject constructor() :
    ListAdapter<Triple<Addresse, Boolean, Boolean>, AddressViewHolder>(diff) {

    private lateinit var _onClick: (Addresse) -> Unit
    fun setOnClick(onClick: (Addresse) -> Unit) {
        _onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AddressViewHolder(
        ItemAddressCheckoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), _onClick
    )

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class AddressViewHolder(
    private val binding: ItemAddressCheckoutBinding,
    private val onClick: (Addresse) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(address: Triple<Addresse, Boolean, Boolean>) {
        binding.run {
            tvName.text = address.first.fullName
            tvAddress.text = address.first.detailAddress
            tvPhone.text = address.first.phoneNumber
            viewSelected.isVisible = address.second
            tvDefault.isVisible = address.third

            root.setOnClickListener {
                onClick(address.first)
            }
        }
    }
}
