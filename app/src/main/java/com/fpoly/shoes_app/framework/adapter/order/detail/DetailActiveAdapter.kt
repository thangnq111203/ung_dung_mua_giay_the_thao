package com.fpoly.shoes_app.framework.adapter.order.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.domain.model.history.OrderStatusDetail

class DetailActiveAdapter (private var orderActiveDetailShoes: List<OrderStatusDetail>?): RecyclerView.Adapter<DetailActiveAdapter.DetailShoeViewHolder>()  {

    inner class DetailShoeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameActiveDetailTextView: TextView = itemView.findViewById(R.id.nameActiveDetail)
        private val contentActiveDetailTextView: TextView = itemView.findViewById(R.id.contentActiveDetail)
        private val dateActiveDetailTextView: TextView = itemView.findViewById(R.id.dateActiveDetail)


        fun bind(historyShoe: OrderStatusDetail) {
            nameActiveDetailTextView.text = historyShoe.status
            contentActiveDetailTextView.text = historyShoe.note
            dateActiveDetailTextView.text = historyShoe.timestamp

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailShoeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_active_infor, parent, false)
        return DetailShoeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DetailShoeViewHolder, position: Int) {
        holder.bind(orderActiveDetailShoes!![position])
    }

    override fun getItemCount() = orderActiveDetailShoes!!.size


}