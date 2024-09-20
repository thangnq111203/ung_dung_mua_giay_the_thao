package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutArgs(
    val carts: Carts? = null,
    val totalCart: Long = 0,
) : Parcelable