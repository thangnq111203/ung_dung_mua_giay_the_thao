package com.fpoly.shoes_app.framework.presentation.ui.favorites

import com.fpoly.shoes_app.framework.domain.model.Category
import com.fpoly.shoes_app.framework.domain.model.Shoes

data class FavoritesUiState(
    val categoriesSelected: List<Pair<Category, Boolean>>? = emptyList(),
    val shoes: List<Shoes>? = emptyList(),
    val favoriteShoes: List<Shoes>? = emptyList(),
    val category: String? = null,
    val isLoadingShoes: Boolean = false,
    val isLoadingCategories: Boolean = false,
    val isLoadingFavorite: Boolean = false,
) {
    val isLoading get() = isLoadingShoes || isLoadingCategories || isLoadingFavorite
    val isVisibleTextEmpty get() = favoriteShoes.isNullOrEmpty() && isLoading.not()
}