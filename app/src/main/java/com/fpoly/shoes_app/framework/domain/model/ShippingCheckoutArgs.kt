package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShippingCheckoutArgs(
    val isHaNoi: Boolean? = false,
    val shipSelected: String? = null,
) : Parcelable