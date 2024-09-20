package com.fpoly.shoes_app.framework.domain.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(

    @SerializedName("orderId")
    val orderId: String,

    @SerializedName("userId")
    val userId: String?,

    @SerializedName("nameOrder")
    val nameOrder: String?,

    @SerializedName("phoneNumber")
    val phoneNumber: Long?,

    @SerializedName("addressOrder")
    val addressOrder: String?,

    @SerializedName("total")
    val total: Double?,

    @SerializedName("dateOrder")
    val dateOrder: String?,

    @SerializedName("pay")
    val pay: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("orderStatusDetails")
    val orderStatusDetails: List<OrderStatusDetail> = emptyList(),

    @SerializedName("discountId")
    val discountId: String?
) : Parcelable

@Parcelize
data class OrderStatusDetail(
    @SerializedName("status")
    val status: String,

    @SerializedName("timestamp")
    val timestamp: String?,

    @SerializedName("note")
    val note: String
) : Parcelable