package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("userName")
    val isUser: String? = null,
    @SerializedName("rateNumber")
    val rate: Int? = 0
) : Parcelable