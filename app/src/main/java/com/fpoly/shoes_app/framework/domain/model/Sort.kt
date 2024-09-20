package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sort(
    val id: Int? = 0,
    val text: String? = null,
) : Parcelable