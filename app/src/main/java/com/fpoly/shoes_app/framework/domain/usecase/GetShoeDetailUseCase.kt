package com.fpoly.shoes_app.framework.domain.usecase

import com.fpoly.shoes_app.framework.data.repository.ShoesRepository
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class GetShoeDetailUseCase @Inject constructor(
    private val shoesRepository: ShoesRepository
) {
    suspend operator fun invoke(id: String) = try {
        Resource.success(shoesRepository.getShoeDetail(id).body())
    } catch (e: Exception) {
        Resource.error(null, e.message)
    }
}