package com.fpoly.shoes_app.framework.presentation.ui.shoes.shoesdetail

import android.os.Parcelable
import com.fpoly.shoes_app.framework.domain.model.CartRequest
import com.fpoly.shoes_app.framework.domain.model.Color
import com.fpoly.shoes_app.framework.domain.model.Shoes
import com.fpoly.shoes_app.framework.domain.model.Size
import com.fpoly.shoes_app.framework.domain.model.StorageShoe
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoeDetailContact(
    val userId: String? = null,
    val shoeDetail: Shoes? = null,
    val isLoading: Boolean? = false,
    val countShoe: Int = 0,
    val sizes: List<Pair<Size, Boolean>>? = emptyList(),
    val colors: List<Pair<Color, Boolean>>? = emptyList(),
    val sizeSelected: Pair<Size, Boolean>? = null,
    val colorSelected: Pair<Color, Boolean>? = null,
) : Parcelable {
    val sold get() = shoeDetail?.quantity?.minus(shoeDetail.sell ?: 0)
    val sizeStore
        get() = shoeDetail?.storageShoe
            ?.filter {
                it.sizeShoe?.size == sizeSelected?.first?.size &&
                        it.colorShoe?.textColor == colorSelected?.first?.textColor
            }
            ?.checkShoes() ?: 0
    val isButtonEnable get() = countShoe > 0
    val priceTotal get() = (shoeDetail?.price ?: 0) * countShoe
    val isCountEnable get() = sizeSelected?.second == true && colorSelected?.second == true
}

fun ShoeDetailContact.addShoeToCart(): CartRequest = CartRequest(
    userId = this.userId.orEmpty(),
    shoeId = this.shoeDetail?.id.orEmpty(),
    sizeId = this.sizeSelected?.first?.id.orEmpty(),
    colorId = this.colorSelected?.first?.id.orEmpty(),
    numberShoe = this.countShoe,
)

private fun List<StorageShoe>?.checkShoes() =
    this?.map { it.quantity?.minus(it.quantity.minus(it.sell ?: 0)) }
        ?.fold(0) { acc, value -> acc.plus(value ?: 0) } ?: 0