package com.fpoly.shoes_app.framework.presentation.ui.checkout.payment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.OrderRequest
import com.fpoly.shoes_app.framework.domain.usecase.AddOrderUseCase
import com.fpoly.shoes_app.utility.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentCheckoutViewModel @Inject constructor(
    private val addOrderUseCase: AddOrderUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentCheckoutUiState())
    val uiState: StateFlow<PaymentCheckoutUiState> get() = _uiState

    private val _singleEvent = MutableStateFlow<PaymentCheckoutSingleEvent?>(null)
    val singleEvent: Flow<PaymentCheckoutSingleEvent> get() = _singleEvent.filterNotNull()

    private val args = PaymentCheckoutFragmentArgs.fromSavedStateHandle(savedStateHandle).args

    init {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(args = args)
            }
        }
    }

    fun handleCheckout(type: String) {
        flow {
            emit(
                addOrderUseCase.invoke(
                    OrderRequest(
                        addressId = _uiState.value.args?.idAddress,
                        totalShip = _uiState.value.args?.totalShip ?: 0L,
                        total = _uiState.value.args?.total ?: 0L,
                        pay = type,
                        items = _uiState.value.shoeOrder,
                    )
                )
            )
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> _singleEvent.emit(PaymentCheckoutSingleEvent.CheckOut)

                Status.ERROR -> Log.e(
                    "PaymentCheckoutViewModel", "handleCheckout: Error ${resource.message}"
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