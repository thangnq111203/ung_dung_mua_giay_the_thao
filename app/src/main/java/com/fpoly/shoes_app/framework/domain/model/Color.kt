package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Color(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("textColor")
    val textColor: String? = null,
    @SerializedName("codeColor")
    val codeColor: String? = null,
) : Parcelable