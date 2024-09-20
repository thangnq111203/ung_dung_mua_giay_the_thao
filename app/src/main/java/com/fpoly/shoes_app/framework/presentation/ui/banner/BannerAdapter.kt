package com.fpoly.shoes_app.framework.presentation.ui.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.ItemBannerViewBinding
import com.fpoly.shoes_app.framework.domain.model.Banners
import com.fpoly.shoes_app.utility.loadImage
import javax.inject.Inject

private val bannersDiff = object : DiffUtil.ItemCallback<Banners>() {
    override fun areItemsTheSame(oldItem: Banners, newItem: Banners) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Banners, newItem: Banners) = oldItem == newItem
}

class BannerAdapter @Inject constructor() :
    ListAdapter<Banners, BannerViewHolder>(bannersDiff) {

    private lateinit var _onClick: (Banners) -> Unit

    fun setOnClick(onClick: (Banners) -> Unit) {
        _onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BannerViewHolder(
        ItemBannerViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), _onClick
    )

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class BannerViewHolder(
    private val binding: ItemBannerViewBinding,
    private val onClick: (Banners) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(banner: Banners) {
        binding.run {
            imgThumbnail.loadImage(banner.imageThumbnail)
            tvThumbnail.text = banner.thumbnail
            root.setOnClickListener { onClick(banner) }
        }
    }
}