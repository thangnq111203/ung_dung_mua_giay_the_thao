package com.fpoly.shoes_app.framework.presentation.ui.checkout.address

import com.fpoly.shoes_app.framework.domain.model.profile.address.Addresse
import com.fpoly.shoes_app.utility.ADDRESS_DEFAULT

data class AddressCheckoutContract(
    val isLoading: Boolean = false,
    val address: List<Addresse>? = emptyList(),
    val isSelected: String? = null,
) {
    val addressPair = address?.map {
        Triple(it, it.id == isSelected, it.permission == ADDRESS_DEFAULT)
    }
    val isVisibleTextEmpty get() = addressPair.isNullOrEmpty() && isLoading.not()
}