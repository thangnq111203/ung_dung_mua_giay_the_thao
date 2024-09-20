package com.fpoly.shoes_app.framework.adapter.order

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.domain.model.history.HistoryShoe
import com.fpoly.shoes_app.utility.formatToVND

class HistoryAdapter(
    private var historyShoes: List<HistoryShoe>,
    private val onClickActive: ((HistoryShoe) -> Unit)?,
    private val onClickComplete: ((HistoryShoe) -> Unit?)?
) : RecyclerView.Adapter<HistoryAdapter.HistoryShoeViewHolder>() {

    inner class HistoryShoeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageShoeItemImageView: ImageView = itemView.findViewById(R.id.imageShoeItem)
        private val nameShoeTextView: TextView = itemView.findViewById(R.id.nameShoeItem)

        private val priceShoeTextView: TextView = itemView.findViewById(R.id.priceShoeItem)
        private val rateOrTrackShoe: TextView = itemView.findViewById(R.id.rateOrTrack)
        private val wait: TextView = itemView.findViewById(R.id.wait)
        @SuppressLint("SetTextI18n")
        fun bind(historyShoe: HistoryShoe) {
            wait.text = historyShoe.orderStatusDetails?.last()?.status.orEmpty()
            nameShoeTextView.text = historyShoe.orderDetails?.get(0)?.name ?: "Order"
            rateOrTrackShoe.text = itemView.context.getString(
                when(historyShoe.status){
                    "completed" -> R.string.leaveReview
                else -> R.string.description
            })

            if (historyShoe.statusNumber == 5)
                wait.visibility = View.VISIBLE
            if (historyShoe.statusNumber == 6) {
                rateOrTrackShoe.text = itemView.context.getString(R.string.saveCancel)
                rateOrTrackShoe.isEnabled = false
            }
            priceShoeTextView.text = historyShoe.total.toString().formatToVND()
            Glide.with(itemView.context)
                .load(historyShoe.thumbnail)
                .into(imageShoeItemImageView)


            rateOrTrackShoe.setOnClickListener {
                onClickComplete?.invoke(historyShoe)
                onClickActive?.invoke(historyShoe)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryShoeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryShoeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryShoeViewHolder, position: Int) {
        holder.bind(historyShoes[position])
    }

    override fun getItemCount() = historyShoes.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateHistoryShoes(newHistoryShoes: List<HistoryShoe>) {
        historyShoes = newHistoryShoes
        notifyDataSetChanged()
    }
}