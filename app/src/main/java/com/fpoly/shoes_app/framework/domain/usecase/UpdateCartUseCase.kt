package com.fpoly.shoes_app.framework.domain.usecase

import android.util.Log
import com.fpoly.shoes_app.framework.data.repository.CartRepository
import com.fpoly.shoes_app.framework.domain.model.UpdateCartRequest
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend operator fun invoke(id: String, numberShoe: UpdateCartRequest) = try {
        Log.d("123123", "invoke: $id, $numberShoe")
        Resource.success(cartRepository.updateCart(id, numberShoe))
    } catch (e: Exception) {
        Resource.error(null, e.message)
    }
}