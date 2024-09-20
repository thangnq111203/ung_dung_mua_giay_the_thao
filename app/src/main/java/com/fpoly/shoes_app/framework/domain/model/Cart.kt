package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Carts(
    @SerializedName("carts")
    val cart: List<ShoesCart>? = emptyList(),
) : Parcelable

@Parcelize
data class ShoesCart(
    @SerializedName("shoe")
    val shoe: ShoeData? = null,
) : Parcelable

@Parcelize
data class ShoeData(
    @SerializedName("cartId")
    val id: String? = null,
    @SerializedName("shoeId")
    val idShoe: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("price")
    val price: Long? = 0L,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("size")
    val size: Size? = null,
    @SerializedName("color")
    val color: ColorCart? = null,
    @SerializedName("numberShoe")
    val numberShoe: Int? = 0,
) : Parcelable

@Parcelize
data class ColorCart(
    @SerializedName("colorId")
    val id: String? = null,
    @SerializedName("textColor")
    val textColor: String? = null,
    @SerializedName("codeColor")
    val codeColor: String? = null,
) : Parcelable



