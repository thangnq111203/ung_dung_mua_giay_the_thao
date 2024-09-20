package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shoes(
    @SerializedName("rateShoe")
    var rate: Rate? = null,
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("shoeId")
    val shoeId: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("price")
    val price: Long? = 0L,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("status")
    val status: Int? = 0, // 0: Active, 1: Inactive, 2: Sold
    @SerializedName("typerShoe")
    val category: Category? = null,
    @SerializedName("imageShoe")
    val imagesShoe: List<String>? = emptyList(),
    @SerializedName("sizeShoe")
    val sizes: List<Size>? = emptyList(),
    @SerializedName("colorShoe")
    val colors: List<Color>? = emptyList(),
    @SerializedName("storageShoe")
    val storageShoe: List<StorageShoe>? = emptyList(),
    @SerializedName("importQuanlityAll")
    val quantity: Int? = 0,
    @SerializedName("soldQuanlityAll")
    val sell: Int? = 0,
    @SerializedName("createDate")
    val created: String? = null,
    @SerializedName("updateDate")
    val updated: String? = null,
) : Parcelable