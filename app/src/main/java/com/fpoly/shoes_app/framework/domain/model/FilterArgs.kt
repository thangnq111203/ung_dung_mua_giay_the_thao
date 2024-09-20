package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterArgs(
    val idCategory: String?,
    val startPrice: Long?,
    val endPrice: Long?,
    val idSort: Int?,
    val rating: Int?,
) : Parcelable