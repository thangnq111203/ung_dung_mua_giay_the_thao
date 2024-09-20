package com.fpoly.shoes_app.framework.data.dataremove.api

import com.fpoly.shoes_app.framework.domain.model.Discounts
import retrofit2.Response
import retrofit2.http.GET

interface DiscountApi {
    @GET("listDiscount")
    suspend fun getDiscounts(): Response<Discounts>
}