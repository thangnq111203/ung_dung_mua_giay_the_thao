package com.fpoly.shoes_app.framework.domain.usecase

import com.fpoly.shoes_app.framework.data.repository.BannerRepository
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class GetBannerUseCase @Inject constructor(private val bannerRepository: BannerRepository) {
    suspend operator fun invoke() = try {
        Resource.success(bannerRepository.getBanner().body()?.data?.filter { it.hide == false })
    } catch (e: Exception) {
        Resource.error(null, e.message)
    }
}