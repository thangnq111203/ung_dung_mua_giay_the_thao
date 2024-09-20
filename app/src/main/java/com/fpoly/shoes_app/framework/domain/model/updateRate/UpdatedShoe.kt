package com.fpoly.shoes_app.framework.domain.model.updateRate

data class UpdatedShoe(
    val __v: Int,
    val _id: String,
    val colorShoe: List<String>,
    val createDate: String,
    val description: String,
    val gender: Int,
    val imageShoe: List<String>,
    val importPrice: Int,
    val importQuanlityAll: Int,
    val name: String,
    val price: Int,
    val rateShoe: RateShoeX,
    val sellQuanlityAll: Int,
    val shoeId: String,
    val sizeShoe: List<String>,
    val status: Int,
    val storageShoe: List<StorageShoe>,
    val thumbnail: String,
    val typerShoe: String
)