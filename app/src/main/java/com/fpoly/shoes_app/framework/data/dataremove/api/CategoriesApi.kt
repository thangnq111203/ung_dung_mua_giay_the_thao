package com.fpoly.shoes_app.framework.data.dataremove.api

import com.fpoly.shoes_app.framework.domain.model.Category
import retrofit2.Response
import retrofit2.http.GET

interface CategoriesApi {

    // get all types (categories)
    @GET("getalltype")
    suspend fun getCategories(): Response<List<Category>>
}