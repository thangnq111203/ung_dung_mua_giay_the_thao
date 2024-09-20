package com.fpoly.shoes_app.framework.presentation.ui.checkout.shipping

import com.fpoly.shoes_app.framework.domain.model.Ship

data class ShippingCheckoutUiState(
    val isLoading: Boolean = false,
    val shipsHaNoi: List<Ship> = emptyList(),
    val shipsOut: List<Ship> = emptyList(),
    val isHaNoi: Boolean? = false,
    val isSelected: String? = null,
) {
    private val ships = (if (isHaNoi == true) shipsHaNoi else shipsOut)
    val shipsPair = ships.map { Pair(it, it.id == isSelected) }
}