package com.fpoly.shoes_app.framework.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.RateInterface
import com.fpoly.shoes_app.framework.domain.model.updateRate.UpdateRate
import javax.inject.Inject

class RateRepository  @Inject constructor(
    private val apiService: RateInterface
) {
    suspend fun rateUpdate(rate: UpdateRate) = apiService.rateOrder(rate)
}