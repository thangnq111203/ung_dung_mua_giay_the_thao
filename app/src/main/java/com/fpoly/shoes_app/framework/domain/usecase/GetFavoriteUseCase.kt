package com.fpoly.shoes_app.framework.domain.usecase

import com.fpoly.shoes_app.framework.data.repository.FavoriteRepository
import com.fpoly.shoes_app.utility.InActive
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class GetFavoriteUseCase @Inject constructor(private val favoriteRepository: FavoriteRepository) {
    suspend operator fun invoke(id: String) = try {
        Resource.success(
            favoriteRepository.getFavorite(id)
                .body()?.favoritesData?.shoeId?.filter { it.status != InActive })
    } catch (e: Exception) {
        Resource.error(null, e.message)
    }
}