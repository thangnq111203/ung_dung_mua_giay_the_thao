package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.OrderApi
import com.fpoly.shoes_app.framework.domain.model.OrderRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderRepository @Inject constructor(private val orderApi: OrderApi) {
    suspend fun addOrder(orderRequest: OrderRequest) = withContext(Dispatchers.IO) {
        orderApi.addOrder(orderRequest)
    }
}