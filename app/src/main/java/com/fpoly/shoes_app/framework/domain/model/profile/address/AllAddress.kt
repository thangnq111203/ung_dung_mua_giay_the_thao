package com.fpoly.shoes_app.framework.domain.model.profile.address

data class AllAddressResponse(
    val addresses: List<Addresse>?,
    val message: String,
    val success: Boolean
)
