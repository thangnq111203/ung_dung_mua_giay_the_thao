package com.fpoly.shoes_app.framework.presentation.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.ItemCategorySelectedViewBinding
import com.fpoly.shoes_app.framework.domain.model.Category
import javax.inject.Inject

private val categoriesSelectedDiff = object : DiffUtil.ItemCallback<Pair<Category, Boolean>>() {
    override fun areItemsTheSame(
        oldItem: Pair<Category, Boolean>, newItem: Pair<Category, Boolean>
    ) = oldItem.first.id == newItem.first.id

    override fun areContentsTheSame(
        oldItem: Pair<Category, Boolean>, newItem: Pair<Category, Boolean>
    ) = oldItem == newItem
}

class CategoriesSelectedAdapter @Inject constructor() :
    ListAdapter<Pair<Category, Boolean>, CategoriesSelectedViewHolder>(categoriesSelectedDiff) {

    private lateinit var _onClick: (Category) -> Unit

    fun setOnClick(onClick: (Category) -> Unit) {
        _onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoriesSelectedViewHolder(
            ItemCategorySelectedViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), _onClick
        )

    override fun onBindViewHolder(holder: CategoriesSelectedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CategoriesSelectedViewHolder(
    private val binding: ItemCategorySelectedViewBinding,
    private val onClick: (Category) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: Pair<Category, Boolean>) {
        binding.tvCategorySelected.run {
            text = category.first.name
            setOnClickListener {
                onClick(category.first)
            }
            if (category.second) setLayout(R.color.primary_white, R.drawable.bg_category_selected)
            else setLayout(R.color.primary_black, R.drawable.bg_category_unselected)
        }
    }

    private fun TextView.setLayout(textColor: Int, setBackground: Int) {
        setTextColor(ContextCompat.getColor(context, textColor))
        background = ContextCompat.getDrawable(context, setBackground)
    }
}