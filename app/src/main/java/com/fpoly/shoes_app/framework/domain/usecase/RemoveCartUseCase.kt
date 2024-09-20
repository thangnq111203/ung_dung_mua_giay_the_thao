package com.fpoly.shoes_app.framework.domain.usecase

import com.fpoly.shoes_app.framework.data.repository.CartRepository
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class RemoveCartUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend operator fun invoke(id: String) = try {
        Resource.success(cartRepository.removeCart(id))
    } catch (e: Exception) {
        Resource.error(null, e.message)
    }
}