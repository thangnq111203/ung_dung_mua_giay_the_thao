package com.fpoly.shoes_app.framework.presentation.ui.splash

import com.fpoly.shoes_app.framework.domain.model.PageSplash

data class SplashUiState(
    val pagesSplash: List<PageSplash>? = emptyList(),
    val page: Int = 0,
    val textButton: String? = null
)

sealed class SplashSingleEvent {
    data object GoToHome : SplashSingleEvent()
    data object GoToSignIn : SplashSingleEvent()
}