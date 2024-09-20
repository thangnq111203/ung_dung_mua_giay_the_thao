package com.fpoly.shoes_app.framework.data.dataremove.api.postInterface

import com.fpoly.shoes_app.framework.domain.model.CancelReason
import com.fpoly.shoes_app.framework.domain.model.history.confirmTake.ConfirmTakeModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface CancleInterface {
    @PUT("cancelOrder/{id}")
suspend fun cancelOrder(@Path("id") id: String,@Body cancelReason: CancelReason): Response<ConfirmTakeModel>
}

