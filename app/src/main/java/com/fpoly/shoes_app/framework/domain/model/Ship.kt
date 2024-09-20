package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ship(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val price: Long? = 0L,
) : Parcelable