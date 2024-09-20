package com.fpoly.shoes_app.framework.presentation.ui.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.ItemSplashViewBinding
import com.fpoly.shoes_app.framework.domain.model.PageSplash
import com.fpoly.shoes_app.utility.loadImage
import javax.inject.Inject

private val pageSplashDiff = object : DiffUtil.ItemCallback<PageSplash>() {
    override fun areItemsTheSame(oldItem: PageSplash, newItem: PageSplash) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PageSplash, newItem: PageSplash) = oldItem == newItem
}

class SplashAdapter @Inject constructor() :
    ListAdapter<PageSplash, PagerSplashViewHolder>(pageSplashDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PagerSplashViewHolder(
            ItemSplashViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: PagerSplashViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PagerSplashViewHolder(
    private val binding: ItemSplashViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(pager: PageSplash) {
        binding.run {
            tvTitle.text = pager.title
            tvContent.text = pager.content
            imgBackground.loadImage(pager.background)
        }
    }
}