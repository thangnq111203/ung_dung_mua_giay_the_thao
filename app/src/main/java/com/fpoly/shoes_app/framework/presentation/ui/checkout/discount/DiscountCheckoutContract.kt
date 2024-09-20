package com.fpoly.shoes_app.framework.presentation.ui.checkout.discount

import com.fpoly.shoes_app.framework.domain.model.Discount

data class DiscountCheckoutUiState(
    val isLoading: Boolean = false,
    val discounts: List<Discount>? = emptyList(),
    val isSelected: String? = null,
) {
    val discountsPair = discounts?.map { Pair(it, it.id == isSelected) }
    val isVisibleTextEmpty get() = discountsPair.isNullOrEmpty() && isLoading.not()
}