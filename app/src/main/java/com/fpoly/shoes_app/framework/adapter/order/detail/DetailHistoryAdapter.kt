package com.fpoly.shoes_app.framework.adapter.order.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.domain.model.history.OrderDetail
import com.fpoly.shoes_app.utility.formatToVND

class DetailHistoryAdapter(private var orderDetailShoes: List<OrderDetail>?): RecyclerView.Adapter<DetailHistoryAdapter.DetailShoeViewHolder>()  {

    inner class DetailShoeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageShoeItemImageView: ImageView = itemView.findViewById(R.id.imageShoeActive)
        private val nameShoeTextView: TextView = itemView.findViewById(R.id.nameShoeActive)
        private val colorShoeTextView: TextView = itemView.findViewById(R.id.colorShoeActive)
        private val sizeShoeTextView: TextView = itemView.findViewById(R.id.sizeShoeActive)
        private val priceShoeTextView: TextView = itemView.findViewById(R.id.priceShoeActive)
        private val amongShoeTextView: TextView = itemView.findViewById(R.id.amongShoeActive)

        fun bind(historyShoe: OrderDetail) {
            nameShoeTextView.text = historyShoe.name
            amongShoeTextView.text = historyShoe.quantity.toString()
            priceShoeTextView.text = historyShoe.price.toString().formatToVND()
            Glide.with(itemView.context)
                .load(historyShoe.thumbnail)
                .into(imageShoeItemImageView)
                colorShoeTextView.text = historyShoe.textColor
                sizeShoeTextView.text = historyShoe.size
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailShoeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_history_infor, parent, false)
        return DetailShoeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DetailShoeViewHolder, position: Int) {
        holder.bind(orderDetailShoes!![position])
    }

    override fun getItemCount() = orderDetailShoes!!.size


}