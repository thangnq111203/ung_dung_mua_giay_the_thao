package com.fpoly.shoes_app.framework.presentation.ui.shoes.shoesdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.ItemColorShoeDetailViewBinding
import com.fpoly.shoes_app.framework.domain.model.Color
import com.fpoly.shoes_app.framework.presentation.ui.shoes.shoesdetail.ShoeDetailFragment.Companion.IS_WHITE_COLOR
import javax.inject.Inject

private val colorShoeDiff = object : DiffUtil.ItemCallback<Pair<Color, Boolean>>() {
    override fun areItemsTheSame(oldItem: Pair<Color, Boolean>, newItem: Pair<Color, Boolean>) =
        oldItem.first.id == newItem.first.id

    override fun areContentsTheSame(oldItem: Pair<Color, Boolean>, newItem: Pair<Color, Boolean>) =
        oldItem == newItem
}

class ColorsShoeDetailAdapter @Inject constructor() :
    ListAdapter<Pair<Color, Boolean>, ColorsShoeViewHolder>(colorShoeDiff) {
    private lateinit var _onClick: (Color) -> Unit

    fun setOnClick(onClick: (Color) -> Unit) {
        _onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsShoeViewHolder =
        ColorsShoeViewHolder(
            ItemColorShoeDetailViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), _onClick
        )

    override fun onBindViewHolder(holder: ColorsShoeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ColorsShoeViewHolder(
    private val binding: ItemColorShoeDetailViewBinding,
    private val onClick: (Color) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(color: Pair<Color, Boolean>) {
        binding.run {
            val codeColor = color.first.codeColor
            root.setOnClickListener { onClick(color.first) }
            root.background.setTint(android.graphics.Color.parseColor(codeColor))
            imgCheck.isVisible = color.second
            view.isVisible = codeColor?.contains(IS_WHITE_COLOR, ignoreCase = true) ?: false
            imgCheck.isSelected = codeColor?.contains(IS_WHITE_COLOR, ignoreCase = true) ?: false
        }
    }
}
