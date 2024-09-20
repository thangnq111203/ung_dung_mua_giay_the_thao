package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentArgs(
    val idAddress: String? = null,
    val shoesCart: List<ShoesCart>? = emptyList(),
    val totalShip: Long? = 0L,
    val total: Long? = 0L,
) : Parcelable

