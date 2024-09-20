package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Favorites(
    @SerializedName("favourites")
    val favoritesData: FavoritesData? = null,
) : Parcelable

@Parcelize
data class FavoritesData(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("shoeId")
    val shoeId: List<Shoes>? = emptyList(),
    @SerializedName("userId")
    val userId: UserId? = null,
) : Parcelable

@Parcelize
data class UserId(
    @SerializedName("_id")
    val id: String? = null,
) : Parcelable

