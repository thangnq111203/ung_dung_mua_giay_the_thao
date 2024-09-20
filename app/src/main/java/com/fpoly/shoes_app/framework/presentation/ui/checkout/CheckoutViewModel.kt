package com.fpoly.shoes_app.framework.presentation.ui.checkout

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.Discount
import com.fpoly.shoes_app.framework.domain.model.Ship
import com.fpoly.shoes_app.framework.domain.model.profile.address.Addresse
import com.fpoly.shoes_app.framework.domain.usecase.GetAddressDefaultUseCase
import com.fpoly.shoes_app.framework.domain.usecase.GetShoeDetailUseCase
import com.fpoly.shoes_app.utility.SharedPreferencesManager
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getAddressDefaultUseCase: GetAddressDefaultUseCase,
    private val sharedPreferences: SharedPreferencesManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> get() = _uiState

    private val args = CheckoutFragmentArgs.fromSavedStateHandle(savedStateHandle).args

    init {
        handleAddressDefault()
        handleCard()
    }

    private fun handleAddressDefault() {
        flow {
            emit(getAddressDefaultUseCase.invoke(sharedPreferences.getIdUser()))
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> _uiState.update {
                    it.copy(addressDefault = resource.data?.firstOrNull())
                }

                Status.ERROR -> Log.e(
                    "CheckoutViewModel", "handleAddressDefault: Error ${resource.message}"
                )

                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoadingAddress = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoadingAddress = false) }
        }.launchIn(viewModelScope)
    }

    private fun handleCard() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    carts = args.carts,
                    totalCart = args.totalCart,
                )
            }
        }
    }

    fun handleShipSelected(ship: Ship?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(ship = ship)
            }
        }
    }

    fun handleDiscountSelected(discount: Discount?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(discount = discount)
            }
        }
    }

    fun handleAddressSelected(address: Addresse?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(address = address)
            }
        }
    }

    fun clearDiscount() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(discount = null)
            }
        }
    }
}