package com.fpoly.shoes_app.framework.data.dataremove.api

import com.fpoly.shoes_app.framework.domain.model.OrderRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderApi {

    @POST("createOrder")
    suspend fun addOrder(
        @Body orderRequest: OrderRequest,
    ): Response<Any>
}