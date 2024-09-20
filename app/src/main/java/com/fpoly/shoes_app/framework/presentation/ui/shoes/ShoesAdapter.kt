package com.fpoly.shoes_app.framework.presentation.ui.shoes

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

private val shoesDiff = object : DiffUtil.ItemCallback<Pair<Shoes, Boolean>>() {
    override fun areItemsTheSame(oldItem: Pair<Shoes, Boolean>, newItem: Pair<Shoes, Boolean>) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Pair<Shoes, Boolean>, newItem: Pair<Shoes, Boolean>) =
        oldItem == newItem
}

class ShoesAdapter @Inject constructor() :
    ListAdapter<Pair<Shoes, Boolean>, ShoesViewHolder>(shoesDiff) {
    private lateinit var _onClick: (Shoes) -> Unit

    fun setOnClick(onClick: (Shoes) -> Unit) {
        _onClick = onClick
    }

    private lateinit var _onClickFavorite: (Pair<Shoes, Boolean>) -> Unit

    fun setOnClickFavorite(onClick: (Pair<Shoes, Boolean>) -> Unit) {
        _onClickFavorite = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ShoesViewHolder(
        ItemShoeViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), _onClick, _onClickFavorite
    )

    override fun onBindViewHolder(holder: ShoesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ShoesViewHolder(
    private val binding: ItemShoeViewBinding,
    private val onClick: (Shoes) -> Unit,
    private val onClickFavorite: (Pair<Shoes, Boolean>) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(shoes: Pair<Shoes, Boolean>) {
        binding.run {
            imgShoe.loadImage(shoes.first.thumbnail)
            tvNameShoe.text = shoes.first.name
            tvRateShoe.text = "${shoes.first.rate?.rate}"
            tvSoldShoe.text = shoes.first.quantity?.minus(shoes.first.sell ?: 0)?.formatSoldShoe()
            tvPriceShoe.text = shoes.first.price?.formatPriceShoe()
            imgFavorite.isSelected = shoes.second
            cvImageShoe.setOnClickListener { onClick(shoes.first) }
            imgFavorite.setOnClickListener { onClickFavorite(shoes) }
        }
    }
}
