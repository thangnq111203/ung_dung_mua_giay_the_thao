package com.fpoly.shoes_app.framework.domain.model.updateRate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateRate(
    val commentText: String,
    val rateNumber: Int,
    val shoeId: List<String>,
    val oderId: String?,
    val userId: String
): Parcelable
