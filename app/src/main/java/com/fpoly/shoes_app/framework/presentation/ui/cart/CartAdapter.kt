package com.fpoly.shoes_app.framework.presentation.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.ItemMyCartBinding
import com.fpoly.shoes_app.framework.domain.model.ShoeData
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

class CartAdapter @Inject constructor() : ListAdapter<ShoesCart, CartViewHolder>(cartsDiff) {
    private lateinit var _onClick: (String) -> Unit
    fun setOnClick(onClick: (String) -> Unit) {
        _onClick = onClick
    }

    private lateinit var _onClickRemove: (String) -> Unit
    fun setOnClickRemove(onClick: (String) -> Unit) {
        _onClickRemove = onClick
    }

    private lateinit var _onClickPlus: (ShoeData?) -> Unit
    fun setOnClickPlus(onClick: (ShoeData?) -> Unit) {
        _onClickPlus = onClick
    }

    private lateinit var _onClickReduce: (ShoeData?) -> Unit
    fun setOnClickReduce(onClick: (ShoeData?) -> Unit) {
        _onClickReduce = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CartViewHolder(
        ItemMyCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
        _onClick, _onClickRemove, _onClickPlus, _onClickReduce
    )

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CartViewHolder(
    private val binding: ItemMyCartBinding,
    private val onClick: (String) -> Unit,
    private val onClickRemove: (String) -> Unit,
    private val onClickPlus: (ShoeData?) -> Unit,
    private val onClickReduce: (ShoeData?) -> Unit,
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

            cvImageShoe.setOnClickListener { onClick(shoes.shoe?.idShoe.orEmpty()) }
            imgDelete.setOnClickListener { onClickRemove(shoes.shoe?.id.orEmpty()) }
            imgPlus.setOnClickListener { onClickPlus(shoes.shoe) }
            imgReduce.setOnClickListener { onClickReduce(shoes.shoe) }
        }
    }
}
