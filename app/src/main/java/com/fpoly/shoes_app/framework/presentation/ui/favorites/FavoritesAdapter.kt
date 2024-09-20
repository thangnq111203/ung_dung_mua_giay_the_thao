package com.fpoly.shoes_app.framework.presentation.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.ItemShoeViewBinding
import com.fpoly.shoes_app.framework.domain.model.Shoes
import com.fpoly.shoes_app.utility.formatPriceShoe
import com.fpoly.shoes_app.utility.formatSoldShoe
import com.fpoly.shoes_app.utility.loadImage
import javax.inject.Inject

private val shoesDiff = object : DiffUtil.ItemCallback<Shoes>() {
    override fun areItemsTheSame(oldItem: Shoes, newItem: Shoes) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Shoes, newItem: Shoes) = oldItem == newItem
}

class FavoriteAdapter @Inject constructor() : ListAdapter<Shoes, FavoriteViewHolder>(shoesDiff) {
    private lateinit var _onClick: (Shoes) -> Unit

    private lateinit var _onClickFavorite: (Shoes) -> Unit

    fun setOnClick(onClick: (Shoes) -> Unit) {
        _onClick = onClick
    }

    fun setOnClickFavorite(onClick: (Shoes) -> Unit) {
        _onClickFavorite = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FavoriteViewHolder(
        ItemShoeViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), _onClick, _onClickFavorite
    )

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class FavoriteViewHolder(
    private val binding: ItemShoeViewBinding,
    private val onClick: (Shoes) -> Unit,
    private val onClickFavorite: (Shoes) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(shoes: Shoes) {
        binding.run {
            imgShoe.loadImage(shoes.thumbnail)
            tvNameShoe.text = shoes.name
            tvRateShoe.text = "${shoes.rate?.rate}"
            tvSoldShoe.text = shoes.quantity?.minus(shoes.sell ?: 0)?.formatSoldShoe()
            tvPriceShoe.text = shoes.price?.formatPriceShoe()
            imgFavorite.isSelected = true
            cvImageShoe.setOnClickListener { onClick(shoes) }
            imgFavorite.setOnClickListener { onClickFavorite(shoes) }
        }
    }
}
