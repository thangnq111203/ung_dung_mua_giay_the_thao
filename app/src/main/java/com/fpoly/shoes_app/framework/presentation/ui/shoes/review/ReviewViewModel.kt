package com.fpoly.shoes_app.framework.presentation.ui.shoes.review

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.usecase.GetReviewUseCase
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
class ReviewViewModel @Inject constructor(
    private val getReviewUseCase: GetReviewUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> get() = _uiState

    private val args = ReviewFragmentArgs.fromSavedStateHandle(savedStateHandle).args

    init {
        getReviews()
    }

    private fun getReviews() {
        flow {
            emit(getReviewUseCase.invoke(args))
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    _uiState.update { state ->
                        state.copy(
                            reviews = resource.data?.comment,
                            rate = resource.data?.starRate,
                        )
                    }
                }

                Status.ERROR -> Log.e("ReviewViewModel", "getReviews: Error ${resource.message}")

                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoading = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }.launchIn(viewModelScope)
    }
}