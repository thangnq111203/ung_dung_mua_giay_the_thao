package com.fpoly.shoes_app.framework.presentation.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.ShoeData
import com.fpoly.shoes_app.framework.domain.model.UpdateCartRequest
import com.fpoly.shoes_app.framework.domain.usecase.GetCartUseCase
import com.fpoly.shoes_app.framework.domain.usecase.RemoveCartUseCase
import com.fpoly.shoes_app.framework.domain.usecase.UpdateCartUseCase
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
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val removeCartUseCase: RemoveCartUseCase,
    private val sharedPreferences: SharedPreferencesManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> get() = _uiState

    init {
        getDataCart()
    }

    fun getDataCart() {
        flow {
            emit(getCartUseCase.invoke(sharedPreferences.getIdUser()))
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> _uiState.update { state ->
                    state.copy(carts = resource.data)
                }

                Status.ERROR -> Log.e(
                    "CartViewModel", "getDataCart: Error ${resource.message}"
                )

                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoading = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }.launchIn(viewModelScope)
    }

    fun removeShoeCart(id: String) {
        flow {
            emit(removeCartUseCase.invoke(id))
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> getDataCart()

                Status.ERROR -> Log.e(
                    "CartViewModel", "removeShoeCart: Error ${resource.message}"
                )

                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoading = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }.launchIn(viewModelScope)
    }

    fun updateShoe(shoesCart: ShoeData?, type: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    numberShoe = shoesCart?.numberShoe ?: 0,
                    type = type
                )
            }
            updateShoeCart(shoesCart?.id.orEmpty())
        }
    }

    private fun updateShoeCart(id: String) {
        flow {
            emit(
                updateCartUseCase.invoke(
                    id = id,
                    numberShoe = UpdateCartRequest(
                        uiState.value.numberCheck,
                    )
                )
            )
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    getDataCart()
                }

                Status.ERROR -> Log.e(
                    "CartViewModel", "updateShoeCart: Error ${resource.message}"
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