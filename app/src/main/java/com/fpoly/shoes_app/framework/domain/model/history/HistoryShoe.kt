package com.fpoly.shoes_app.framework.domain.model.history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryShoe(
    val _id: String?,
    val addressOrder: String?,
    val shoeId: String?,
    val dateOrder: String?,
    val nameOrder: String?,
    val orderDetails: List<OrderDetail>?,
    val orderStatusDetails: List<OrderStatusDetail>?,
    val pay: String?,
    val phoneNumber: String?,
    val promo: Int?,
    val status: String?,
    val statusNumber: Int?,
    val thumbnail: String?,
    val total: Int?,
    val totalPre: Int?,
) : Parcelable