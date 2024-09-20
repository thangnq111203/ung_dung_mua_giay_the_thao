package com.fpoly.shoes_app.framework.data.dataremove.api.getInterface

import com.fpoly.shoes_app.framework.domain.model.profile.address.AllAddressResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AlladdressInterface {
    @GET("findaddress/{id}")
    suspend fun allAddress(@Path("id") id: String): Response<AllAddressResponse>
}