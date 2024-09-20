package com.fpoly.shoes_app.framework.domain.model.history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetail(
    val _id: String?,
    val amount: Int?,
    val codeColor: String?,
    val name: String?,
    val shoeId: String?,
    val price: Int?,
    val quantity: Int?,
    val size: String?,
    val textColor: String?,
    val thumbnail: String?
):Parcelable