package com.fpoly.shoes_app.framework.domain.usecase

import com.fpoly.shoes_app.framework.data.repository.ShoesRepository
import com.fpoly.shoes_app.utility.InActive
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class GetShoesUseCase @Inject constructor(private val shoesRepository: ShoesRepository) {
    suspend operator fun invoke() = try {
        Resource.success(shoesRepository.getShoes().body()?.filter { it.status != InActive })
    } catch (e: Exception) {
        Resource.error(null, e.message)
    }
}