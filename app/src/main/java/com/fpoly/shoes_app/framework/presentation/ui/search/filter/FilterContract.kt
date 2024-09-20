package com.fpoly.shoes_app.framework.presentation.ui.search.filter

import com.fpoly.shoes_app.framework.domain.model.Category
import com.fpoly.shoes_app.framework.domain.model.Sort
import com.fpoly.shoes_app.utility.RatingText
import com.fpoly.shoes_app.utility.SortText

data class FilterUiState(
    val isLoading: Boolean = false,
    val categories: List<Pair<Category, Boolean>>? = emptyList(),
    val categorySelected: String? = null,
    val sorts: List<Pair<Sort, Boolean>>? = emptyList(),
    val sortSelected: Int? = SortText.MOST_RECENT,
    val ratings: List<Pair<Int, Boolean>>? = emptyList(),
    val ratingSelected: Int? = RatingText.RATING_ALL,
    val priceMin: Long = 0L,
    val priceMaxCheck: Long = 0L,
) {
    val priceMax = if (priceMaxCheck >= priceMin) priceMaxCheck else priceMin
}