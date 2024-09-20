package com.fpoly.shoes_app.framework.data.dataremove.api.postInterface

import com.fpoly.shoes_app.framework.domain.model.login.LoginResponse
import com.fpoly.shoes_app.framework.domain.model.profile.address.AllAddressResponse
import com.fpoly.shoes_app.framework.domain.model.profile.address.addAddress.AddAddress
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AddAddressInterface {
        @POST("addaddress")
        suspend fun addAddress(@Body addAddressRequest: AddAddress): Response<AllAddressResponse>
    }