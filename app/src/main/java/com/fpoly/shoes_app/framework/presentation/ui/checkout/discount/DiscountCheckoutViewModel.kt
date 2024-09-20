package com.fpoly.shoes_app.framework.presentation.ui.checkout.discount

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.usecase.GetDiscountsUseCase
import com.fpoly.shoes_app.utility.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DiscountCheckoutViewModel @Inject constructor(
    private val getDiscountsUseCase: GetDiscountsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiscountCheckoutUiState())
    val uiState: StateFlow<DiscountCheckoutUiState> get() = _uiState

    private val args = DiscountCheckoutFragmentArgs.fromSavedStateHandle(savedStateHandle).args

    init {
        getDiscounts()
    }

    private fun getDiscounts() {
        flow {
            emit(getDiscountsUseCase.invoke())
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> _uiState.update {
                    it.copy(
                        discounts = resource.data,
                        isSelected = args,
                    )
                }

                Status.ERROR -> Log.e(
                    "DiscountCheckoutViewModel", "getDiscounts: Error ${resource.message}"
                )

                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoading = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }.launchIn(viewModelScope)
    }
}