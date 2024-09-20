package com.fpoly.shoes_app.framework.adapter.address

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.domain.model.profile.address.Addresse

class AddressAdapter(
    private val addressDetails: MutableList<Addresse>?,
    private val onAddressClick: (Addresse) -> Unit,
    private val onEditClick: (Addresse) -> Unit
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameUserTextView: TextView = itemView.findViewById(R.id.nameUser)
        private val numberUserTextView: TextView = itemView.findViewById(R.id.numberPhoneUser)
        private val nameAddresseTextView: TextView = itemView.findViewById(R.id.nameAddress)
        private val detailAddressTextView: TextView = itemView.findViewById(R.id.detailAddress)
        private val imageEdit: ImageView = itemView.findViewById(R.id.imageEdit)

        fun bind(addressDetail: Addresse) {
            nameUserTextView.text = addressDetail.fullName
            numberUserTextView.text = addressDetail.phoneNumber.toString()
            nameAddresseTextView.text = addressDetail.nameAddress
            detailAddressTextView.text = addressDetail.detailAddress
            itemView.setOnClickListener {
                onAddressClick(addressDetail)
            }
            imageEdit.setOnClickListener {
                onEditClick(addressDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return AddressViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addressDetails!![position])
    }

    override fun getItemCount() = addressDetails!!.size
    fun deleteItem(position: Int) {
        addressDetails!!.removeAt(position)
        notifyItemRemoved(position)
    }
}
