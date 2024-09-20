package com.fpoly.shoes_app.framework.presentation.ui.shoes

import android.os.Parcelable
import com.fpoly.shoes_app.framework.domain.model.Category
import com.fpoly.shoes_app.framework.domain.model.Shoes
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoesUiState(
    val categoriesSelected: List<Pair<Category, Boolean>>? = emptyList(),
    val popularShoes: List<Shoes>? = emptyList(),
    val favoriteShoes: List<Shoes>? = emptyList(),
    val isLoadingShoes: Boolean = false,
    val isLoadingCategories: Boolean = false,
    val isLoadingFavorite: Boolean = false,
) : Parcelable {
    val isLoading get() = isLoadingShoes || isLoadingCategories || isLoadingFavorite
    val shoes: List<Pair<Shoes, Boolean>>
        get() {
            val favoriteId = favoriteShoes?.map { it.id }?.toSet() ?: emptySet()
            return popularShoes?.map { shoe ->
                shoe to favoriteId.contains(shoe.id)
            } ?: emptyList()
        }
    val isVisibleTextEmpty get() = shoes.isEmpty() && isLoading.not()
}