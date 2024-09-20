package com.fpoly.shoes_app.framework.data.dataremove.api.postInterface

import com.fpoly.shoes_app.framework.domain.model.history.confirmTake.ConfirmTakeModel
import retrofit2.Response
import retrofit2.http.PUT
import retrofit2.http.Path

interface ConfirmTakeInterface {
    @PUT("confirmReceived/{id}")
    suspend fun confirmTake(@Path("id") id: String): Response<ConfirmTakeModel>
}