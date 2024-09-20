package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.DiscountApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DiscountsRepository @Inject constructor(private val discountApi: DiscountApi) {
    suspend fun getDiscounts() = withContext(Dispatchers.IO) {
        discountApi.getDiscounts()
    }
}