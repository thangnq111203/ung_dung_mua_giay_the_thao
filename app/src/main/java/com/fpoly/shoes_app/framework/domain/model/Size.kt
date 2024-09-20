package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Size(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("size")
    val size: String? = null,
) : Parcelable