package com.fpoly.shoes_app.framework.data.dataremove.api.postInterface

import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMail
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ForgotMailInterface {
    @POST("sendotpmail")
    suspend fun forgotMail(@Body forgotMailRequest: ForgotMail): Response<ForgotMailResponse>
}