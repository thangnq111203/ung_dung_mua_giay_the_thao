package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.deleteInterface.DeleteaddressInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.AddAddressInterface
import com.fpoly.shoes_app.framework.domain.model.profile.address.addAddress.AddAddress
import javax.inject.Inject

class AddAddressRepository  @Inject constructor(
    private val apiService: AddAddressInterface
) {
    suspend fun addAddress(address: AddAddress) = apiService.addAddress(address)
}