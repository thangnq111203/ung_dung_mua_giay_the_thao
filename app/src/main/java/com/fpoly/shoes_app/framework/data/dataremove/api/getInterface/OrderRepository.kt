package com.fpoly.shoes_app.framework.data.dataremove.api.getInterface

import com.fpoly.shoes_app.framework.domain.model.history.HistoryShoe


interface OrderRepository {
    suspend fun getActiveOrders(userId: String): List<HistoryShoe>
    suspend fun getCompletedOrders(userId: String): List<HistoryShoe>
}