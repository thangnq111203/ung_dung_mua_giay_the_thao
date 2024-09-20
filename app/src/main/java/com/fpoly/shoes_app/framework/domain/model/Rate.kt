package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rate(
    @SerializedName("comment")
    val comments: List<Comment>? = emptyList(),
    @SerializedName("starRate")
    val rate: Float? = 0F
) : Parcelable