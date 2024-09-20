package com.fpoly.shoes_app.framework.presentation.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.ItemCategoryViewBinding
import com.fpoly.shoes_app.framework.domain.model.Category
import com.fpoly.shoes_app.utility.ITEM_MORE
import com.fpoly.shoes_app.utility.loadImage
import javax.inject.Inject

private val categoriesDiff = object : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem
}

class CategoriesAdapter @Inject constructor() :
    ListAdapter<Category, CategoriesViewHolder>(categoriesDiff) {

    private lateinit var _onClick: (Category) -> Unit

    fun setOnClick(onClick: (Category) -> Unit) {
        _onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoriesViewHolder(
        ItemCategoryViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), _onClick
    )

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CategoriesViewHolder(
    private val binding: ItemCategoryViewBinding,
    private val onClick: (Category) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: Category) {
        binding.run {
            if (category.image == ITEM_MORE) {
                imgCategory.loadImage(R.drawable.ic_more)
            } else {
                imgCategory.loadImage(category.image)
            }
            tvCategory.text = category.name
            root.setOnClickListener { onClick(category) }
        }
    }
}