package com.fpoly.shoes_app.framework.domain.model.history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderStatusDetail(
    val _id: String?,
    val note: String?,
    val status: String?,
    val timestamp: String?
):Parcelable