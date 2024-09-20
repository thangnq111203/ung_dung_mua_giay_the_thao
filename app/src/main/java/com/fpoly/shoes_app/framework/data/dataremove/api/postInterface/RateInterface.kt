package com.fpoly.shoes_app.framework.data.dataremove.api.postInterface

import com.fpoly.shoes_app.framework.domain.model.updateRate.UpdateRate
import com.fpoly.shoes_app.framework.domain.model.updateRate.UpdateRateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RateInterface {
    @POST("updateOrderAndRateShoe")
    suspend fun rateOrder(@Body rate: UpdateRate): Response<UpdateRateResponse>
}
