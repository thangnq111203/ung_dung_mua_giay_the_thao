package com.fpoly.shoes_app.framework.data.dataremove.api

import com.fpoly.shoes_app.framework.domain.model.Shoes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ShoesApi {

    //get all shoes
    @GET("getallproduct")
    suspend fun getShoes(): Response<List<Shoes>>

    // get shoe detail with id
    @GET("findproduct/{id}")
    suspend fun getShoeDetail(
        @Path("id") id: String
    ): Response<Shoes>
}