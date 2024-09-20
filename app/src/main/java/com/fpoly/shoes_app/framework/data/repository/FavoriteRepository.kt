package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.FavoriteApi
import com.fpoly.shoes_app.framework.domain.model.FavoritesRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val favoriteApi: FavoriteApi) {
    suspend fun addFavorite(favoriteRequest: FavoritesRequest) = withContext(Dispatchers.IO) {
        favoriteApi.addFavorite(favoriteRequest)
    }

    suspend fun removeFavorite(favoriteRequest: FavoritesRequest) = withContext(Dispatchers.IO) {
        favoriteApi.removeFavorite(favoriteRequest)
    }

    suspend fun getFavorite(id: String) = withContext(Dispatchers.IO) {
        favoriteApi.getFavorite(id)
    }
}