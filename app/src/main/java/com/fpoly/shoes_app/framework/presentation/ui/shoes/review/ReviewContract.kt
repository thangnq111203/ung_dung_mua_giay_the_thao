package com.fpoly.shoes_app.framework.presentation.ui.shoes.review

import com.fpoly.shoes_app.framework.domain.model.ReviewDetail

data class ReviewUiState(
    val isLoading: Boolean = false,
    val reviews: List<ReviewDetail>? = emptyList(),
    val rate: Float? = 0F,
)