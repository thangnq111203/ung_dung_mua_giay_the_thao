package com.fpoly.shoes_app.framework.presentation.ui.checkout.payment

import com.fpoly.shoes_app.framework.domain.model.CardOrder
import com.fpoly.shoes_app.framework.domain.model.PaymentArgs
import com.fpoly.shoes_app.framework.domain.model.ShoeData
import com.fpoly.shoes_app.framework.domain.model.ShoesCart

data class PaymentCheckoutUiState(
    val isLoading: Boolean = false,
    val args: PaymentArgs? = null,
) {
    val shoeOrder = args?.shoesCart?.toListOrderRequest() ?: emptyList()
}

fun List<ShoesCart>.toListOrderRequest(): List<CardOrder> =
    this.map { it.shoe.toOrderRequest() }

fun ShoeData?.toOrderRequest(): CardOrder = CardOrder(
    cartId = this?.id.orEmpty(),
)

sealed interface PaymentCheckoutSingleEvent {
    data object CheckOut : PaymentCheckoutSingleEvent
}