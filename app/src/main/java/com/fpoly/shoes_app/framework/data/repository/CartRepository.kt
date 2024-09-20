package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.CartApi
import com.fpoly.shoes_app.framework.domain.model.CartRequest
import com.fpoly.shoes_app.framework.domain.model.UpdateCartRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepository @Inject constructor(private val cartApi: CartApi) {
    suspend fun addCart(cartRequest: CartRequest) = withContext(Dispatchers.IO) {
        cartApi.addCart(cartRequest)
    }

    suspend fun updateCart(id: String, numberShoe: UpdateCartRequest) =
        withContext(Dispatchers.IO) {
            cartApi.updateCart(id = id, numberShoe = numberShoe)
        }

    suspend fun removeCart(id: String) = withContext(Dispatchers.IO) {
        cartApi.removeCart(id)
    }

    suspend fun getCart(id: String) = withContext(Dispatchers.IO) {
        cartApi.getCart(id)
    }
}