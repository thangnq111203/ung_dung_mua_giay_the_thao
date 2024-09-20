package com.fpoly.shoes_app.framework.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.OrderApiService
import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.OrderRepository
import com.fpoly.shoes_app.framework.domain.model.history.HistoryShoe
import javax.inject.Inject

class OrderRepositoryImpl  @Inject constructor(
    private val orderApiService: OrderApiService
) : OrderRepository {

    override suspend fun getActiveOrders(userId: String): List<HistoryShoe> {
        return orderApiService.getActiveOrders(userId)
    }

    override suspend fun getCompletedOrders(userId: String): List<HistoryShoe> {
        return orderApiService.getCompletedOrders(userId)
    }
}