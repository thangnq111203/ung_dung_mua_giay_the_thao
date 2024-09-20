package com.fpoly.shoes_app.framework.presentation.ui.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.ItemMyCartBinding
import com.fpoly.shoes_app.framework.domain.model.ShoesCart
import com.fpoly.shoes_app.framework.presentation.ui.shoes.shoesdetail.ShoeDetailFragment
import com.fpoly.shoes_app.utility.formatPriceShoe
import com.fpoly.shoes_app.utility.loadImage
import com.fpoly.shoes_app.utility.toTotal
import javax.inject.Inject

private val cartsDiff = object : DiffUtil.ItemCallback<ShoesCart>() {
    override fun areItemsTheSame(oldItem: ShoesCart, newItem: ShoesCart) =
        oldItem.shoe?.id == newItem.shoe?.id

    override fun areContentsTheSame(oldItem: ShoesCart, newItem: ShoesCart) =
        oldItem.shoe == newItem.shoe
}

class CartCheckoutAdapter @Inject constructor() :
    ListAdapter<ShoesCart, CartViewHolder>(cartsDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CartViewHolder(
        ItemMyCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CartViewHolder(
    private val binding: ItemMyCartBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(shoes: ShoesCart) {
        binding.run {
            imgShoe.loadImage(shoes.shoe?.thumbnail)
            tvNameShoe.text = shoes.shoe?.name
            viewColor.background.setTint(
                android.graphics.Color.parseColor((shoes.shoe?.color?.codeColor))
            )
            viewColorWhite.isVisible = shoes.shoe?.color?.codeColor?.contains(
                ShoeDetailFragment.IS_WHITE_COLOR,
                ignoreCase = true
            ) ?: false
            tvColor.text = shoes.shoe?.color?.textColor
            tvSize.text = shoes.shoe?.size?.size
            tvPrice.text = shoes.shoe?.numberShoe?.toTotal(shoes.shoe.price)?.formatPriceShoe()
            tvNumberQuantity.text = shoes.shoe?.numberShoe.toString()

            imgDelete.isVisible = false
            imgPlus.isVisible = false
            imgReduce.isVisible = false
        }
    }
}
