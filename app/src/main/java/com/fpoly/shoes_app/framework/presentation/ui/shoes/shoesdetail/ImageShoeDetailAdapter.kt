package com.fpoly.shoes_app.framework.presentation.ui.shoes.shoesdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.ItemImageShoeDetailViewBinding
import com.fpoly.shoes_app.utility.loadImage
import javax.inject.Inject

private val imageShoeDiff = object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}

class ImageShoeDetailAdapter @Inject constructor() :
    ListAdapter<String, ImageShoeViewHolder>(imageShoeDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageShoeViewHolder =
        ImageShoeViewHolder(
            ItemImageShoeDetailViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ImageShoeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ImageShoeViewHolder(
    private val binding: ItemImageShoeDetailViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(image: String) {
        binding.run {
            imageShoeDetail.loadImage(image)
        }
    }
}