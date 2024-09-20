package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import com.fpoly.shoes_app.framework.domain.model.user.User
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    @SerializedName("shoeId")
    val shoeId: String? = null,
    @SerializedName("rateShoe")
    val rateShoe: Reviews? = null,
) : Parcelable

@Parcelize
data class Reviews(
    @SerializedName("comment")
    val comment: List<ReviewDetail>? = emptyList(),
    @SerializedName("starRate")
    val starRate: Float? = 0F,
) : Parcelable

@Parcelize
data class ReviewDetail(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("userName")
    val userName: User? = null,
    @SerializedName("commetText")
    val comment: String? = null,
    @SerializedName("rateNumber")
    val rateNumber: Int? = 0,
    @SerializedName("createDate")
    val date: String? = null,
) : Parcelable
