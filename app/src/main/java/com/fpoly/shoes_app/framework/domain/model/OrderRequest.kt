package com.fpoly.shoes_app.framework.domain.model

data class OrderRequest(
    val addressId: String? = null,
    val pay: String,
    val total: Long,
    val totalShip: Long,
    val items: List<CardOrder>,
)

data class CardOrder(
    val cartId: String,
)