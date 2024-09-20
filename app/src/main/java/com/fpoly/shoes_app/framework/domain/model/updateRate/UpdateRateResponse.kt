package com.fpoly.shoes_app.framework.domain.model.updateRate

data class UpdateRateResponse(
    val message: String,
    val error: String?,
    val updatedOrders: List<UpdatedOrder>?,
    val updatedShoes: List<UpdatedShoe>?
)