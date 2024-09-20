package com.fpoly.shoes_app.framework.data.dataremove.api.getInterface

import com.fpoly.shoes_app.framework.domain.model.history.HistoryShoe
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderApiService {
    @GET("getUserActiveOrders/{userId}")
    suspend fun getActiveOrders(
        @Path("userId") userId: String
    ): List<HistoryShoe>

    @GET("getUserCompletedOrders/{userId}")
    suspend fun getCompletedOrders(
        @Path("userId") userId: String
    ): List<HistoryShoe>
}
