package com.fpoly.shoes_app.framework.presentation.ui.search.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.ItemRatingViewBinding
import com.fpoly.shoes_app.utility.formatRating
import javax.inject.Inject


private val diff = object : DiffUtil.ItemCallback<Pair<Int, Boolean>>() {
    override fun areItemsTheSame(
        oldItem: Pair<Int, Boolean>, newItem: Pair<Int, Boolean>
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: Pair<Int, Boolean>, newItem: Pair<Int, Boolean>
    ) = oldItem == newItem
}

class RatingAdapter @Inject constructor() :
    ListAdapter<Pair<Int, Boolean>, RatingViewHolder>(diff) {

    private lateinit var _onClick: (Int) -> Unit

    fun setOnClick(onClick: (Int) -> Unit) {
        _onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RatingViewHolder(
            ItemRatingViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), _onClick
        )

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class RatingViewHolder(
    private val binding: ItemRatingViewBinding,
    private val onClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Pair<Int, Boolean>) {
        binding.run {
            tvRating.text = item.first.formatRating()
            root.setOnClickListener {
                onClick(item.first)
            }
            tvRating.setTextColor(
                ContextCompat.getColor(
                    root.context,
                    if (item.second) R.color.primary_white
                    else R.color.primary_black
                )
            )
            imgRating.isSelected = item.second
            root.run {
                background = ContextCompat.getDrawable(
                    context,
                    if (item.second) R.drawable.bg_category_selected
                    else R.drawable.bg_category_unselected
                )
            }
        }
    }
}