package com.fpoly.shoes_app.framework.domain.usecase

import com.fpoly.shoes_app.framework.data.repository.DiscountsRepository
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class GetDiscountsUseCase @Inject constructor(private val discountsRepository: DiscountsRepository) {
    suspend operator fun invoke() = try {
        Resource.success(
            discountsRepository.getDiscounts().body()?.discount
                ?.filter { it.isActive == true },
        )
    } catch (e: Exception) {
        Resource.error(null, e.message)
    }
}