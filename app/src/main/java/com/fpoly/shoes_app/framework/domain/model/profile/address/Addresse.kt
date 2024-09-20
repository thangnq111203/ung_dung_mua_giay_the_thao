package com.fpoly.shoes_app.framework.domain.model.profile.address

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Addresse(
    @SerializedName("__v") val v: Int,
    @SerializedName("_id") val id: String,
    val detailAddress: String,
    val latitude: Double,
    val longitude: Double,
    val nameAddress: String?,
    val permission: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
) : Parcelable