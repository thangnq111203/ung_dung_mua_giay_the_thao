package com.fpoly.shoes_app.framework.presentation.ui.search.filter

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.Category
import com.fpoly.shoes_app.framework.domain.model.FilterArgs
import com.fpoly.shoes_app.framework.domain.model.Sort
import com.fpoly.shoes_app.framework.domain.usecase.GetCategoriesUseCase
import com.fpoly.shoes_app.utility.GET_POPULAR_SHOES_ALL
import com.fpoly.shoes_app.utility.RatingText
import com.fpoly.shoes_app.utility.SortText
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
class FilterViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> get() = _uiState

    private val args = FilterFragmentArgs.fromSavedStateHandle(savedStateHandle).args

    private val sortsData = listOf(
        Sort(
            id = SortText.MOST_RECENT,
            text = "Mới nhất",
        ),
        Sort(
            id = SortText.POPULAR,
            text = "Bán chạy",
        ),
        Sort(
            id = SortText.PRICE_LOW,
            text = "Giá giảm dần",
        ),
        Sort(
            id = SortText.PRICE_HIGH,
            text = "Giá tăng dần",
        ),
        Sort(
            id = SortText.RATING,
            text = "Đánh giá",
        ),
    )

    private val ratingData = listOf(
        RatingText.RATING_ALL,
        RatingText.RATING_5,
        RatingText.RATING_4,
        RatingText.RATING_3,
        RatingText.RATING_2,
        RatingText.RATING_1,
    )

    init {
        updateArgsToUiState(args)
        getDataCategories(args.idCategory)
        handleSort(args.idSort)
        handleRating(args.rating)
    }

    private fun updateArgsToUiState(args: FilterArgs) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    categorySelected = args.idCategory,
                    sortSelected = args.idSort,
                    ratingSelected = args.rating,
                    priceMin = args.startPrice ?: 0L,
                    priceMaxCheck = args.endPrice ?: 0L,
                )
            }
        }
    }

    fun resetUiState() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    categorySelected = null,
                    sortSelected = SortText.MOST_RECENT,
                    ratingSelected = RatingText.RATING_ALL,
                    priceMin = 0L,
                    priceMaxCheck = 0L,
                )
            }
        }
        getDataCategories(null)
        handleSort()
        handleRating()
    }

    private fun getDataCategories(id: String? = GET_POPULAR_SHOES_ALL) {
        flow {
            emit(getCategoriesUseCase.invoke())
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> _uiState.update { state ->
                    state.copy(
                        categories = updateCategoriesSelectedList(resource.data).map {
                            if (it.id == id || id == GET_POPULAR_SHOES_ALL) it to true
                            else it to false
                        })
                }

                Status.ERROR -> Log.e(
                    "FilterViewModel", "getDataCategories: Error ${resource.message}"
                )

                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoading = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }.launchIn(viewModelScope)
    }

    private fun updateCategoriesSelectedList(categories: List<Category>?): List<Category> {
        val all = Category(
            name = GET_POPULAR_SHOES_ALL
        )
        val mutableCategoriesSelected = categories.orEmpty().toMutableList()
        mutableCategoriesSelected.add(0, all)
        return mutableCategoriesSelected
    }

    fun handleClickCategoriesSelected(id: String?) {
        val mutableCategoriesSelected = mutableListOf<Pair<Category, Boolean>>()
        uiState.value.categories?.forEach {
            if (it.first.id == id) {
                mutableCategoriesSelected.add(Pair(it.first, true))
            } else {
                mutableCategoriesSelected.add(Pair(it.first, false))
            }
        }
        _uiState.update { state ->
            state.copy(
                categories = mutableCategoriesSelected,
                categorySelected = id,
            )
        }
    }

    private fun handleSort(id: Int? = SortText.MOST_RECENT) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    sorts = sortsData.map {
                        if (it.id == id) it to true
                        else it to false
                    }
                )
            }
        }
    }

    fun handleClickSortSelected(id: Int?) {
        val mutableSortSelected = mutableListOf<Pair<Sort, Boolean>>()
        uiState.value.sorts?.forEach {
            if (it.first.id == id) {
                mutableSortSelected.add(Pair(it.first, true))
            } else {
                mutableSortSelected.add(Pair(it.first, false))
            }
        }
        _uiState.update { state ->
            state.copy(
                sorts = mutableSortSelected,
                sortSelected = id,
            )
        }
    }

    private fun handleRating(text: Int? = RatingText.RATING_ALL) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    ratings = ratingData.map {
                        if (it == text) it to true
                        else it to false
                    }
                )
            }
        }
    }

    fun handleClickRatingSelected(text: Int?) {
        val mutableRatingSelected = mutableListOf<Pair<Int, Boolean>>()
        uiState.value.ratings?.forEach {
            if (it.first == text) {
                mutableRatingSelected.add(Pair(it.first, true))
            } else {
                mutableRatingSelected.add(Pair(it.first, false))
            }
        }
        _uiState.update { state ->
            state.copy(
                ratings = mutableRatingSelected,
                ratingSelected = text,
            )
        }
    }

    fun handlePriceMin(price: String) {
        _uiState.update { state ->
            state.copy(
                priceMin = if (price.isBlank()) 0L else price.toLong(),
            )
        }
    }

    fun handlePriceMax(price: String) {
        _uiState.update { state ->
            state.copy(
                priceMaxCheck = if (price.isBlank()) 0L else price.toLong(),
            )
        }
    }
}