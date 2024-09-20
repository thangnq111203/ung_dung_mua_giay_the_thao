package com.fpoly.shoes_app.framework.domain.model.updateRate

data class UpdatedOrder(
    val newStatus: Int,
    val orderDetails: List<OrderDetail>,
    val orderId: String
)