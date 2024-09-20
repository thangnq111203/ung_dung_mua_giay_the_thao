package com.fpoly.shoes_app.framework.presentation.ui.search

import android.os.Parcelable
import com.fpoly.shoes_app.framework.domain.model.Shoes
import com.fpoly.shoes_app.utility.RatingText
import com.fpoly.shoes_app.utility.SortText
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchUiState(
    val shoesByName: List<Shoes>? = emptyList(),
    val textSearch: String? = null,
    val favoriteShoes: List<Shoes>? = emptyList(),
    val isLoadingShoes: Boolean = false,
    val isLoadingFavorite: Boolean = false,
    val category: String? = null,
    val sort: Int = SortText.MOST_RECENT,
    val rating: Int = RatingText.RATING_ALL,
    val priceMin: Long = 0L,
    val priceMax: Long = 0L,
) : Parcelable {
    val isLoading get() = isLoadingShoes || isLoadingFavorite

    val shoes: List<Pair<Shoes, Boolean>>
        get() {
            val favoriteId = favoriteShoes?.map { it.id }?.toSet() ?: emptySet()
            return shoesByName?.map { shoe ->
                shoe to favoriteId.contains(shoe.id)
            } ?: emptyList()
        }
}