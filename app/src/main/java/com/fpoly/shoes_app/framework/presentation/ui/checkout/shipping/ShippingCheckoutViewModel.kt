package com.fpoly.shoes_app.framework.presentation.ui.checkout.shipping

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.Ship
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ShippingCheckoutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ShippingCheckoutUiState())
    val uiState: StateFlow<ShippingCheckoutUiState> get() = _uiState

    private val args = ShippingCheckoutFragmentArgs.fromSavedStateHandle(savedStateHandle).args

    private val shipsHaNoi = listOf(
        Ship("1", "Hỏa tốc", "Nhận hàng sau 1-2 ngày", 40000),
        Ship("2", "Nhanh", "Nhận hàng sau 3-5 ngày", 20000),
        Ship("3", "Tiếp kiệm", "Nhận hàng sau 5-7 ngày", 15000),
    )

    private val ships = listOf(
        Ship("1", "Nhanh", "Nhận hàng sau 6-8 ngày", 40000),
        Ship("2", "Tiếp kiệm", "Nhận hàng sau 8-10 ngày", 30000),
    )

    init {
        handleShip()
    }

    private fun handleShip() {
        flow {
            _uiState.update {
                it.copy(
                    shipsHaNoi = shipsHaNoi,
                    shipsOut = ships,
                    isHaNoi = args.isHaNoi,
                    isSelected = args.shipSelected,
                )
            }.let { emit(it) }
        }.onStart {
            _uiState.update { it.copy(isLoading = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }.launchIn(viewModelScope)
    }
}