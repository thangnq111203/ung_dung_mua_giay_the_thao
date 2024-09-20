package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("imageType")
    val image: String? = null,
    @SerializedName("nameType")
    val name: String? = null
) : Parcelable