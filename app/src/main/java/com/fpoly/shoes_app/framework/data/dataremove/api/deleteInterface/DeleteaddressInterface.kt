package com.fpoly.shoes_app.framework.data.dataremove.api.deleteInterface

import com.fpoly.shoes_app.framework.domain.model.profile.address.AllAddressResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface DeleteaddressInterface {
    @DELETE("deleteaddress/{id}")
    suspend fun deleteAddress(@Path("id") id: String): Response<AllAddressResponse>
}