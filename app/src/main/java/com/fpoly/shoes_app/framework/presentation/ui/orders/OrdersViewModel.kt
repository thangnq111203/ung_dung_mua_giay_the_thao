package com.fpoly.shoes_app.framework.presentation.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.OrderRepository
import com.fpoly.shoes_app.framework.domain.model.history.HistoryShoe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun getActiveOrders(userId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState(isLoading = true)
                val orders = orderRepository.getActiveOrders(userId)
                _uiState.value = UiState(historyShoes = orders)
            } catch (e: Exception) {
                _uiState.value = UiState(errorMessage = e.message)
            }
        }
    }

    fun getCompletedOrders(userId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState(isLoading = true)
                val orders = orderRepository.getCompletedOrders(userId)
                _uiState.value = UiState(historyShoes = orders)
            } catch (e: Exception) {
                _uiState.value = UiState(errorMessage = e.message)
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val historyShoes: List<HistoryShoe> = emptyList(),
        val errorMessage: String? = null
    )
}