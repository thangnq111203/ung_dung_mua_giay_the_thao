package com.fpoly.shoes_app.framework.domain.usecase

import com.fpoly.shoes_app.framework.data.repository.ReviewRepository
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class GetReviewUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(id: String) = try {
        Resource.success(reviewRepository.getReview(id).body()?.rateShoe)
    } catch (e: Exception) {
        Resource.error(null, e.message)
    }
}