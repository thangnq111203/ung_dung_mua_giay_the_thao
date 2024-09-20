package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.deleteInterface.DeleteaddressInterface
import javax.inject.Inject

class DeleteAddressRepository  @Inject constructor(
    private val apiService: DeleteaddressInterface
) {
    suspend fun deleteAddress(id:String) = apiService.deleteAddress(id)
}