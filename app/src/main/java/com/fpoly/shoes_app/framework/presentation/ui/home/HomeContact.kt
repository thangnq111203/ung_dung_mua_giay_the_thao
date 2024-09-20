package com.fpoly.shoes_app.framework.presentation.ui.home

import android.graphics.Bitmap
import android.os.Parcelable
import com.fpoly.shoes_app.framework.domain.model.Banners
import com.fpoly.shoes_app.framework.domain.model.Category
import com.fpoly.shoes_app.framework.domain.model.Shoes
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeUiState(
    val categories: List<Category>? = emptyList(),
    val categoriesSelected: List<Pair<Category, Boolean>>? = emptyList(),
    val banners: List<Banners>? = emptyList(),
    val popularShoes: List<Shoes>? = emptyList(),
    val favoriteShoes: List<Shoes>? = emptyList(),
    val nameUser: String? = null,
    val imageUser: Bitmap? = null,
    val isLoadingBanners: Boolean = false,
    val isLoadingShoes: Boolean = false,
    val isLoadingCategories: Boolean = false,
    val isLoadingFavorite: Boolean = false,
    val isLoadingUser: Boolean = false,
) : Parcelable {
    val isLoading get() = isLoadingBanners || isLoadingShoes || isLoadingCategories || isLoadingFavorite || isLoadingUser

    val shoes: List<Pair<Shoes, Boolean>>
        get() {
            val favoriteSet = favoriteShoes?.map { it.id }?.toSet() ?: emptySet()
            return popularShoes?.map { shoe ->
                shoe to favoriteSet.contains(shoe.id)
            } ?: emptyList()
        }
}