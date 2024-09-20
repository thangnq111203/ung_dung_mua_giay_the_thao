package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.deleteInterface.DeleteaddressInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.AddAddressInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.UpdateAddressInterface
import com.fpoly.shoes_app.framework.domain.model.profile.address.addAddress.AddAddress
import javax.inject.Inject

class UpdateAddressRepository  @Inject constructor(
    private val apiService: UpdateAddressInterface
) {
    suspend fun updateAddress(id:String,address: AddAddress) = apiService.updateAddress(id,address)
}