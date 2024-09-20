package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Discounts(
    @SerializedName("discounts")
    val discount: List<Discount>? = emptyList(),
) : Parcelable

@Parcelize
data class Discount(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("discountAmount")
    val discount: Int? = 0,
    @SerializedName("formattedEndDate")
    val date: String? = null,
    @SerializedName("isActive")
    val isActive: Boolean? = false,
) : Parcelable