package com.fpoly.shoes_app.framework.presentation.ui.banner

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.usecase.GetBannerUseCase
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
class BannerDetailViewModel @Inject constructor(
    private val getBannerUseCase: GetBannerUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BannerDetailUiState())
    val uiState: StateFlow<BannerDetailUiState> get() = _uiState

    fun getBannerDetail(id: String) {
        flow {
            emit(getBannerUseCase.invoke())
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    val banner = resource.data
                        ?.firstOrNull { it.id == id }
                    _uiState.update { state ->
                        state.copy(
                            title = banner?.title,
                            image = banner?.image,
                            description = banner?.description
                        )
                    }
                }

                Status.ERROR ->
                    Log.e("BannerDetailViewModel", "getDataBanner: Error ${resource.message}")

                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoading = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }.launchIn(viewModelScope)
    }
}