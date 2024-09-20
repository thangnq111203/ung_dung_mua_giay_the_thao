package com.fpoly.shoes_app.framework.presentation.ui.home

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.data.repository.ProfileRepository
import com.fpoly.shoes_app.framework.domain.model.FavoritesRequest
import com.fpoly.shoes_app.framework.domain.model.Category
import com.fpoly.shoes_app.framework.domain.usecase.AddFavoriteUseCase
import com.fpoly.shoes_app.framework.domain.usecase.GetBannerUseCase
import com.fpoly.shoes_app.framework.domain.usecase.GetCategoriesUseCase
import com.fpoly.shoes_app.framework.domain.usecase.GetFavoriteUseCase
import com.fpoly.shoes_app.framework.domain.usecase.GetShoesUseCase
import com.fpoly.shoes_app.framework.domain.usecase.RemoveFavoriteUseCase
import com.fpoly.shoes_app.utility.GET_POPULAR_SHOES_ALL
import com.fpoly.shoes_app.utility.ITEM_MORE
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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getShoesUseCase: GetShoesUseCase,
    private val getBannerUseCase: GetBannerUseCase,
    private val getFavoriteUseCase: GetFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val profileRepository: ProfileRepository,
    private val sharedPreferences: SharedPreferencesManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> get() = _uiState

    init {
        getDataCategories()
        getDataPopularShoes()
        getBannerList()
        getFavorite()
        getProfile()
    }

    private fun getDataCategories() {
        flow {
            emit(getCategoriesUseCase.invoke())
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> _uiState.update { state ->
                    state.copy(categories = updateCategoriesList(resource.data),
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

    private fun updateCategoriesList(categories: List<Category>?): List<Category> {
        val more = Category(
            image = ITEM_MORE,
            name = ITEM_MORE
        )
        return when {
            categories == null -> emptyList()
            categories.size == CATEGORIES_SIZE_EQUALS_8 -> categories.take(CATEGORIES_SIZE_EQUALS_8)
            categories.size > CATEGORIES_SIZE_EQUALS_8 -> categories.take(
                CATEGORIES_SIZE_MORE_THAN_8
            ).plus(more)

            else -> categories
        }
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

    //get shoes if sold > 0 and max 10 item
    fun getDataPopularShoes(category: String? = GET_POPULAR_SHOES_ALL) {
        flow {
            emit(getShoesUseCase.invoke())
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    _uiState.update { state ->
                        val popularShoes = resource.data
                            ?.filter {
                                (it.quantity?.minus(it.sell ?: 0) ?: 0) > 0 &&
                                        (category == GET_POPULAR_SHOES_ALL || it.category?.name == category)
                            }
                            ?.sortedByDescending { it.quantity?.minus(it.sell ?: 0) }
                            ?.take(QUANTITY_POPULAR_SHOES)
                        state.copy(popularShoes = popularShoes)
                    }
                }

                Status.ERROR -> Log.e("HomeViewModel", "getDataShoes: Error ${resource.message}")
                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoadingShoes = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoadingShoes = false) }
        }.launchIn(viewModelScope)
    }

    private fun getBannerList() {
        flow {
            emit(getBannerUseCase.invoke())
        }.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    val banners = resource.data
                    _uiState.update { state ->
                        state.copy(banners = banners)
                    }
                }

                Status.ERROR -> Log.e("HomeViewModel", "getDataBanner: Error ${resource.message}")

                else -> {}
            }
        }.onStart {
            _uiState.update { it.copy(isLoadingBanners = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoadingBanners = false) }
        }.launchIn(viewModelScope)
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

    private fun getProfile() {
        flow {
            emit(profileRepository.profile(sharedPreferences.getIdUser()).body()?.user)
        }.onEach { user ->
            val decodeDataImg =
                Base64.decode(user?.imageAccount?.`$binary`?.base64.toString(), Base64.DEFAULT)
            val image = BitmapFactory.decodeByteArray(decodeDataImg, 0, decodeDataImg.size)
            _uiState.update {
                it.copy(
                    nameUser = user?.fullName,
                    imageUser = image,
                )
            }
        }.onStart {
            _uiState.update { it.copy(isLoadingUser = true) }
        }.onCompletion {
            _uiState.update { it.copy(isLoadingUser = false) }
        }.launchIn(viewModelScope)
    }

    private companion object {
        private const val CATEGORIES_SIZE_EQUALS_8 = 8
        private const val CATEGORIES_SIZE_MORE_THAN_8 = 7
        private const val QUANTITY_POPULAR_SHOES = 10
    }
}