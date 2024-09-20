package com.fpoly.shoes_app.framework.domain.model

data class CartRequest(
    val userId: String,
    val shoeId: String,
    val sizeId: String,
    val colorId: String,
    val numberShoe: Int,
)