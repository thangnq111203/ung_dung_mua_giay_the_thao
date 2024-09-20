package com.fpoly.shoes_app.framework.data.dataremove.api

import com.fpoly.shoes_app.framework.domain.model.Review
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReviewApi {
    @GET("reviewProduct/{id}")
    suspend fun getReview(
        @Path("id") id: String
    ): Response<Review>
}