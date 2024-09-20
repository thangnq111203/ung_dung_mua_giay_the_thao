package com.fpoly.shoes_app.framework.data.dataremove.api

import com.fpoly.shoes_app.framework.domain.model.FavoritesRequest
import com.fpoly.shoes_app.framework.domain.model.Favorites
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteApi {
    @GET("findfavourite/{id}")
    suspend fun getFavorite(
        @Path("id") id: String
    ): Response<Favorites>

    @POST("addfavourite")
    suspend fun addFavorite(
        @Body favoriteRequest: FavoritesRequest,
    ): Response<Any>

    @POST("removefavourite")
    suspend fun removeFavorite(
        @Body favoriteRequest: FavoritesRequest,
    ): Response<Any>
}