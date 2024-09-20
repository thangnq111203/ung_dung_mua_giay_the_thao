package com.fpoly.shoes_app.framework.domain.usecase

import com.fpoly.shoes_app.framework.data.repository.AllAddressRepository
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(private val addressRepository: AllAddressRepository) {
    suspend operator fun invoke(id: String) = try {
        Resource.success(addressRepository.allAddress(id).body()?.addresses)
    } catch (e: Exception) {
        Resource.error(null, e.message)
    }
}