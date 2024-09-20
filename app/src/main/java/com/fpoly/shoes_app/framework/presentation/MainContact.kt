package com.fpoly.shoes_app.framework.presentation

sealed class MainSingleEvent {
    data object ShowErrorFragment : MainSingleEvent()
}