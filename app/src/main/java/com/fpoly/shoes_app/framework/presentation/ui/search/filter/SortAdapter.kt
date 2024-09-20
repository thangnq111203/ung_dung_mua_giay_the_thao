package com.fpoly.shoes_app.framework.presentation.ui.search.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.ItemSortViewBinding
import com.fpoly.shoes_app.framework.domain.model.Sort
import javax.inject.Inject

private val diff = object : DiffUtil.ItemCallback<Pair<Sort, Boolean>>() {
    override fun areItemsTheSame(
        oldItem: Pair<Sort, Boolean>, newItem: Pair<Sort, Boolean>
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: Pair<Sort, Boolean>, newItem: Pair<Sort, Boolean>
    ) = oldItem == newItem
}

class SortAdapter @Inject constructor() :
    ListAdapter<Pair<Sort, Boolean>, SortViewHolder>(diff) {

    private lateinit var _onClick: (Sort) -> Unit

    fun setOnClick(onClick: (Sort) -> Unit) {
        _onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SortViewHolder(
            ItemSortViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), _onClick
        )

    override fun onBindViewHolder(holder: SortViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SortViewHolder(
    private val binding: ItemSortViewBinding,
    private val onClick: (Sort) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Pair<Sort, Boolean>) {
        binding.tvSort.run {
            text = item.first.text
            setOnClickListener {
                onClick(item.first)
            }
            if (item.second) setLayout(R.color.primary_white, R.drawable.bg_category_selected)
            else setLayout(R.color.primary_black, R.drawable.bg_category_unselected)
        }
    }

    private fun TextView.setLayout(textColor: Int, setBackground: Int) {
        setTextColor(ContextCompat.getColor(context, textColor))
        background = ContextCompat.getDrawable(context, setBackground)
    }
}