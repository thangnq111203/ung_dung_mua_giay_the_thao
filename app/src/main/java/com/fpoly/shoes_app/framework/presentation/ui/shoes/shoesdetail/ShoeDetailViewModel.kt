package com.fpoly.shoes_app.framework.presentation.ui.shoes.shoesdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.Color
import com.fpoly.shoes_app.framework.domain.model.Size
import com.fpoly.shoes_app.framework.domain.usecase.AddCartUseCase
import com.fpoly.shoes_app.framework.domain.usecase.GetShoeDetailUseCase
import com.fpoly.shoes_app.framework.presentation.ui.shoes.shoesdetail.ShoeDetailFragment.Companion.MAX_SHOE
import com.fpoly.shoes_app.framework.presentation.ui.shoes.shoesdetail.ShoeDetailFragment.Companion.RESET_COUNT_SHOES
import com.fpoly.shoes_app.utility.PLUS
import com.fpoly.shoes_app.utility.REDUCE
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
class ShoeDetailViewModel @Inject constructor(
    private val getShoeDetailUseCase: GetShoeDetailUseCase,
    private val addCartUseCase: AddCartUseCase,
    private val sharedPreferences: SharedPreferencesManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ShoeDetailContact())
    val uiState: StateFlow<ShoeDetailContact> get() = _uiState

    fun initShoeDetail(id: String) {
        flow {
            emit(getShoeDetailUseCase.invoke(id))
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    _uiState.update { state ->
                        state.copy(
                            userId = sharedPreferences.getIdUser(),
                            shoeDetail = resource.data,
                            sizes = resource.data?.sizes?.map { Pair(it, false) },
                            colors = resource.data?.colors?.map { Pair(it, false) },
                        )
                    }
                }

                Status.ERROR -> {
                    Log.e("ShoeDetailViewModel", "getDataShoe: Error ${resource.message}")
                }

                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoading = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }.launchIn(viewModelScope)
    }

    fun handleCountShoe(type: Int) {
        viewModelScope.launch {
            val countShoe = when (type) {
                REDUCE -> {
                    if (uiState.value.countShoe > 0) {
                        uiState.value.countShoe.minus(1)
                    } else uiState.value.countShoe
                }

                PLUS -> {
                    if (uiState.value.countShoe < uiState.value.sizeStore &&
                        uiState.value.countShoe < MAX_SHOE
                    ) {
                        uiState.value.countShoe.plus(1)
                    } else uiState.value.countShoe
                }

                else -> uiState.value.countShoe
            }
            _uiState.update { it.copy(countShoe = countShoe) }
        }
    }

    fun updateCount(count: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(countShoe = count) }
        }
    }

    fun handleEditTextCount() {
        viewModelScope.launch {
            val countShoe = if (uiState.value.countShoe > uiState.value.sizeStore)
                uiState.value.sizeStore
            else uiState.value.countShoe
            _uiState.update { it.copy(countShoe = countShoe) }
        }
    }

    fun handleClickSize(size: Size) {
        viewModelScope.launch {
            val mutableSizeSelected = mutableListOf<Pair<Size, Boolean>>()
            uiState.value.sizes?.forEach {
                if (it.first == size) {
                    mutableSizeSelected.add(Pair(size, true))
                } else {
                    mutableSizeSelected.add(Pair(it.first, false))
                }
            }
            _uiState.update { state ->
                state.copy(
                    sizes = mutableSizeSelected,
                    sizeSelected = Pair(size, true),
                    countShoe = RESET_COUNT_SHOES,
                )
            }
        }
    }

    fun handleClickColor(color: Color) {
        viewModelScope.launch {
            val mutableColorSelected = mutableListOf<Pair<Color, Boolean>>()
            uiState.value.colors?.forEach {
                if (it.first == color) {
                    mutableColorSelected.add(Pair(color, true))
                } else {
                    mutableColorSelected.add(Pair(it.first, false))
                }
            }
            _uiState.update { state ->
                state.copy(
                    colors = mutableColorSelected,
                    colorSelected = Pair(color, true),
                    countShoe = RESET_COUNT_SHOES,
                )
            }
        }
    }

    fun addShoeToCart() {
        flow {
            emit(addCartUseCase.invoke(cartRequest = uiState.value.addShoeToCart()))
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    _uiState.update {
                        it.copy(countShoe = 0)
                    }
                }

                Status.ERROR -> {
                    Log.e("ShoeDetailViewModel", "addShoeToCart: Error ${resource.message}")
                }

                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoading = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }.launchIn(viewModelScope)
    }
}