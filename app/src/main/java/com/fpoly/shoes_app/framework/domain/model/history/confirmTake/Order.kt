package com.fpoly.shoes_app.framework.domain.model.history.confirmTake

data class Order(
    val __v: Int,
    val _id: String,
    val addressOrder: String,
    val dateOrder: String,
    val discointId: String,
    val nameOrder: String,
    val orderStatusDetails: List<OrderStatusDetail>,
    val pay: String,
    val phoneNumber: Int,
    val status: Int,
    val total: Int,
    val userId: String
)