package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StorageShoe(
    @SerializedName("colorShoe")
    val colorShoe: Color? = null,
    @SerializedName("sizeShoe")
    val sizeShoe: Size? = null,
    @SerializedName("importQuanlity")
    val quantity: Int? = 0,
    @SerializedName("soldQuanlity")
    val sell: Int? = 0,
) : Parcelable