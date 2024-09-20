package com.fpoly.shoes_app.framework.domain.usecase

import com.fpoly.shoes_app.framework.data.repository.OrderRepository
import com.fpoly.shoes_app.framework.domain.model.OrderRequest
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class AddOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(orderRequest: OrderRequest) = try {
        Resource.success(orderRepository.addOrder(orderRequest))
    } catch (e: Exception) {
        Resource.error(null, e.message)
    }
}