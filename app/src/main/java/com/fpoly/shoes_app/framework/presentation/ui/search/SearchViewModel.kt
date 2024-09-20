package com.fpoly.shoes_app.framework.presentation.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.FavoritesRequest
import com.fpoly.shoes_app.framework.domain.model.Shoes
import com.fpoly.shoes_app.framework.domain.usecase.AddFavoriteUseCase
import com.fpoly.shoes_app.framework.domain.usecase.GetFavoriteUseCase
import com.fpoly.shoes_app.framework.domain.usecase.GetShoesSearchByNameUseCase
import com.fpoly.shoes_app.framework.domain.usecase.RemoveFavoriteUseCase
import com.fpoly.shoes_app.utility.GET_POPULAR_SHOES_ALL
import com.fpoly.shoes_app.utility.RatingText
import com.fpoly.shoes_app.utility.SharedPreferencesManager
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
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getShoesSearchByNameUseCase: GetShoesSearchByNameUseCase,
    private val getFavoriteUseCase: GetFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val sharedPreferences: SharedPreferencesManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> get() = _uiState

    init {
        getFavorite()
    }

    fun getDataShoes(
        name: String,
        category: String?,
        sort: Int,
        rating: Int,
        priceMin: Long,
        priceMax: Long,
    ) {
        flow {
            emit(getShoesSearchByNameUseCase.invoke(name))
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    _uiState.update { state ->
                        val data = resource.data
                            ?.filterByCategory(category)
                            ?.filterBySort(sort)
                            ?.filterByRating(rating)
                            ?.filterPrice(priceMin, priceMax)
                        state.copy(
                            shoesByName = data,
                            textSearch = name,
                            category = category,
                            sort = sort,
                            rating = rating,
                            priceMin = priceMin,
                            priceMax = priceMax,
                        )
                    }
                }

                Status.ERROR -> Log.e("SearchViewModel", "getDataShoes: Error ${resource.message}")
                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoadingShoes = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoadingShoes = false) }
        }.launchIn(viewModelScope)
    }

    private fun List<Shoes>.filterByCategory(category: String? = GET_POPULAR_SHOES_ALL): List<Shoes> {
        val categoryCheck = if (category.isNullOrBlank()) GET_POPULAR_SHOES_ALL else category
        return this.filter {
            categoryCheck == GET_POPULAR_SHOES_ALL || it.category?.id == category
        }
    }

    private fun List<Shoes>.filterBySort(sort: Int): List<Shoes> {
        return when (sort) {
            SortText.MOST_RECENT -> {
                this
            }

            SortText.POPULAR -> {
                this.sortedByDescending { shoe ->
                    shoe.quantity?.minus(shoe.sell ?: 0)
                }
            }

            SortText.PRICE_LOW -> {
                this.sortedByDescending { shoe ->
                    shoe.price
                }
            }

            SortText.PRICE_HIGH -> {
                this.sortedBy { shoe ->
                    shoe.price
                }
            }

            SortText.RATING -> {
                this.sortedByDescending { shoe ->
                    shoe.rate?.rate
                }
            }

            else -> this
        }
    }

    private fun List<Shoes>.filterByRating(rating: Int): List<Shoes> {
        return when (rating) {
            RatingText.RATING_ALL -> {
                this
            }

            RatingText.RATING_5 -> {
                this.filter { shoe ->
                    (shoe.rate?.rate ?: 0F) <= rating
                            && (shoe.rate?.rate ?: 0F) >= (rating - 0.5)
                }
            }

            RatingText.RATING_1 -> {
                this.filter { shoe ->
                    (shoe.rate?.rate ?: 0F) < (rating + 0.5)
                            && (shoe.rate?.rate ?: 0F) >= 0
                }
            }

            else -> {
                this.filter { shoe ->
                    (shoe.rate?.rate ?: 0F) < (rating + 0.5)
                            && (shoe.rate?.rate ?: 0F) >= (rating - 0.5)
                }
            }
        }
    }

    private fun List<Shoes>.filterPrice(priceMin: Long, priceMax: Long): List<Shoes> {
        return this.filter { shoe ->
            if (priceMax > 0) {
                (shoe.price ?: 0L) >= priceMin && (shoe.price ?: 0L) <= priceMax
            } else {
                (shoe.price ?: 0L) >= priceMin
            }
        }
    }

    private fun getFavorite() {
        flow {
            emit(getFavoriteUseCase.invoke(sharedPreferences.getIdUser()))
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    _uiState.update { state ->
                        state.copy(favoriteShoes = resource.data)
                    }
                }

                Status.ERROR -> Log.e("HomeViewModel", "getDataShoes: Error ${resource.message}")
                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoadingFavorite = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoadingFavorite = false) }
        }.launchIn(viewModelScope)
    }

    fun addFavorite(id: String) {
        flow {
            addFavoriteUseCase.invoke(
                FavoritesRequest(
                    shoeId = id,
                    userId = sharedPreferences.getIdUser(),
                )
            ).let { emit(it) }
        }.onEach {
            getFavorite()
        }.launchIn(viewModelScope)
    }

    fun deleteFavorite(id: String) {
        flow {
            removeFavoriteUseCase.invoke(
                FavoritesRequest(
                    shoeId = id,
                    userId = sharedPreferences.getIdUser(),
                )
            ).let { emit(it) }
        }.onEach {
            getFavorite()
        }.launchIn(viewModelScope)
    }
}