package com.fpoly.shoes_app.framework.data.dataremove.api.postInterface

import com.fpoly.shoes_app.framework.domain.model.login.LoginResponse
import com.fpoly.shoes_app.framework.domain.model.profile.address.AllAddressResponse
import com.fpoly.shoes_app.framework.domain.model.profile.address.addAddress.AddAddress
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface UpdateAddressInterface {
        @POST("updateaddress/{id}")
        suspend fun updateAddress(@Path("id") id: String,@Body addAddressRequest: AddAddress): Response<AllAddressResponse>
    }