package com.fpoly.shoes_app.framework.presentation.ui.shoes.shoesdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.ItemSizeShoeDetailViewBinding
import com.fpoly.shoes_app.framework.domain.model.Size
import javax.inject.Inject

private val sizeShoeDiff = object : DiffUtil.ItemCallback<Pair<Size, Boolean>>() {
    override fun areItemsTheSame(oldItem: Pair<Size, Boolean>, newItem: Pair<Size, Boolean>) =
        oldItem.first.id == newItem.first.id

    override fun areContentsTheSame(oldItem: Pair<Size, Boolean>, newItem: Pair<Size, Boolean>) =
        oldItem == newItem
}

class SizesShoeDetailAdapter @Inject constructor() :
    ListAdapter<Pair<Size, Boolean>, SizesShoeViewHolder>(sizeShoeDiff) {
    private lateinit var _onClick: (Size) -> Unit

    fun setOnClick(onClick: (Size) -> Unit) {
        _onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesShoeViewHolder =
        SizesShoeViewHolder(
            ItemSizeShoeDetailViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), _onClick
        )

    override fun onBindViewHolder(holder: SizesShoeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SizesShoeViewHolder(
    private val binding: ItemSizeShoeDetailViewBinding,
    private val onClick: (Size) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(size: Pair<Size, Boolean>) {
        binding.run {
            root.setOnClickListener { onClick(size.first) }
            tvSize.text = size.first.size
            root.isSelected = size.second
        }
    }
}