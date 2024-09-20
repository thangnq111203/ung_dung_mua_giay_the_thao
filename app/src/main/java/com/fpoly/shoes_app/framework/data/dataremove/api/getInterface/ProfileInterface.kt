package com.fpoly.shoes_app.framework.data.dataremove.api.getInterface

import com.fpoly.shoes_app.framework.domain.model.profile.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileInterface {
    @GET("finduser/{id}")
    suspend fun profile(@Path("id") id: String): Response<ProfileResponse>
}
