package com.fpoly.shoes_app.framework.presentation.ui.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.Category
import com.fpoly.shoes_app.framework.domain.model.FavoritesRequest
import com.fpoly.shoes_app.framework.domain.usecase.GetCategoriesUseCase
import com.fpoly.shoes_app.framework.domain.usecase.GetFavoriteUseCase
import com.fpoly.shoes_app.framework.domain.usecase.GetShoesUseCase
import com.fpoly.shoes_app.framework.domain.usecase.RemoveFavoriteUseCase
import com.fpoly.shoes_app.utility.GET_POPULAR_SHOES_ALL
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import com.fpoly.shoes_app.utility.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getFavoriteUseCase: GetFavoriteUseCase,
    private val getShoesUseCase: GetShoesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val sharedPreferences: SharedPreferencesManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> get() = _uiState

    init {
        getDataCategories()
        getShoesFavorite()
    }

    private fun getDataCategories() {
        flow {
            emit(getCategoriesUseCase.invoke())
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> _uiState.update { state ->
                    state.copy(
                        categoriesSelected = updateCategoriesSelectedList(resource.data).map {
                            if (it.id.isNullOrEmpty()) it to true
                            else it to false
                        })
                }

                Status.ERROR -> Log.e(
                    "HomeViewModel", "getDataCategories: Error ${resource.message}"
                )

                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoadingCategories = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoadingCategories = false) }
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

    fun handleClickCategoriesSelected(category: Category) {
        val mutableCategoriesSelected = mutableListOf<Pair<Category, Boolean>>()
        uiState.value.categoriesSelected?.forEach {
            if (it.first == category) {
                mutableCategoriesSelected.add(Pair(category, true))
            } else {
                mutableCategoriesSelected.add(Pair(it.first, false))
            }
        }
        _uiState.update { state ->
            state.copy(categoriesSelected = mutableCategoriesSelected)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getShoesFavorite(category: String? = GET_POPULAR_SHOES_ALL) {
        flow {
            emit(getShoesUseCase.invoke().data)
        }.flatMapConcat { shoes ->
            flow {
                val favorites = getFavoriteUseCase.invoke(sharedPreferences.getIdUser()).data
                val favoriteId = favorites?.map { it.id }?.toSet() ?: emptySet()
                emit(shoes?.filter { shoe ->
                    favoriteId.contains(shoe.id)
                } ?: emptyList())
            }
        }.onEach { favorites ->
            _uiState.update { state ->
                state.copy(
                    favoriteShoes = favorites.filter {
                        category == GET_POPULAR_SHOES_ALL || it.category?.name == category
                    },
                    category = category
                )
            }
        }.onStart {
            _uiState.update { it.copy(isLoadingFavorite = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoadingFavorite = false) }
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
            getShoesFavorite(uiState.value.category)
        }.launchIn(viewModelScope)
    }
}