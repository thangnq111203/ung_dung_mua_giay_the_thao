package com.fpoly.shoes_app.framework.data.dataremove.api.postInterface

import com.fpoly.shoes_app.framework.domain.model.signUp.SignUp
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpInterface {
    @POST("register")
    suspend fun signUp(@Body signUpRequest: SignUp): Response<SignUpResponse>
}