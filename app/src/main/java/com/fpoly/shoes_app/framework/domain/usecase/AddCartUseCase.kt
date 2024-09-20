package com.fpoly.shoes_app.framework.domain.usecase

import com.fpoly.shoes_app.framework.data.repository.CartRepository
import com.fpoly.shoes_app.framework.domain.model.CartRequest
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class AddCartUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend operator fun invoke(cartRequest: CartRequest) = try {
        Resource.success(cartRepository.addCart(cartRequest))
    } catch (e: Exception) {
        Resource.error(null, e.message)
    }
}