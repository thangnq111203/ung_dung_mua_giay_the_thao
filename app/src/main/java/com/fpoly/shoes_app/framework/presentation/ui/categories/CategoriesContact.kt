package com.fpoly.shoes_app.framework.presentation.ui.categories

import com.fpoly.shoes_app.framework.domain.model.Category

data class CategoriesUiState(
    val categories: List<Category>? = emptyList(),
    val isLoading: Boolean = false,
) {
    val isVisibleTextEmpty get() = categories.isNullOrEmpty() && isLoading.not()
}