package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Banner(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: List<Banners>? = emptyList(),
) : Parcelable

@Parcelize
data class Banners(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("imageThumbnail")
    val imageThumbnail: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("hide")
    val hide: Boolean? = false,
) : Parcelable