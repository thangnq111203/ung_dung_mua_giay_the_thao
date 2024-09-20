package com.fpoly.shoes_app.framework.presentation.ui.checkout

import com.fpoly.shoes_app.framework.domain.model.Carts
import com.fpoly.shoes_app.framework.domain.model.Discount
import com.fpoly.shoes_app.framework.domain.model.Ship
import com.fpoly.shoes_app.framework.domain.model.profile.address.Addresse
import com.fpoly.shoes_app.utility.HA_NOI
import com.fpoly.shoes_app.utility.IS_100
import com.fpoly.shoes_app.utility.IS_PRICE_NULL
import com.fpoly.shoes_app.utility.formatPriceShoe
import com.fpoly.shoes_app.utility.normalizeString
import com.fpoly.shoes_app.utility.toDiscount

data class CheckoutUiState(
    val isLoadingAddress: Boolean = false,
    val addressDefault: Addresse? = null,
    val carts: Carts? = null,
    val totalCart: Long = 0L,
    val address: Addresse? = null,
    val ship: Ship? = null,
    val discount: Discount? = null,
) {
    val isLoading = isLoadingAddress
    val shoes = carts?.cart
    val addressArgs = address ?: addressDefault
    val detailAddress = address?.detailAddress.orEmpty().ifBlank {
        addressDefault?.detailAddress.orEmpty()
    }
    val nameAddress = address?.fullName.orEmpty().ifBlank {
        addressDefault?.fullName.orEmpty()
    }
    val phoneAddress = address?.phoneNumber.orEmpty().ifBlank {
        addressDefault?.phoneNumber.orEmpty()
    }
    val isAddressNull = detailAddress.isBlank() || nameAddress.isBlank() || phoneAddress.isBlank()
    val shipTotal = ship?.price ?: 0L
    private val discountTotal =
        (discount?.discount ?: 0) * totalCart / IS_100
    val shipTotalString = if (shipTotal == 0L) IS_PRICE_NULL else shipTotal.formatPriceShoe()
    val discountTotalString = if (discountTotal == 0L) IS_PRICE_NULL
    else discountTotal.formatPriceShoe().toDiscount()
    val isEnableClear = discount != null
    val totalString = (totalCart + shipTotal - discountTotal).formatPriceShoe()
    val isEnableButton = totalCart != 0L && shipTotal != 0L
    val isHaNoi = detailAddress.normalizeString().contains(HA_NOI)
}

