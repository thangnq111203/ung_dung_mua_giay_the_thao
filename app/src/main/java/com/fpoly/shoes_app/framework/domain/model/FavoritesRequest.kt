package com.fpoly.shoes_app.framework.domain.model

data class FavoritesRequest(
    val userId: String,
    val shoeId: String,
)