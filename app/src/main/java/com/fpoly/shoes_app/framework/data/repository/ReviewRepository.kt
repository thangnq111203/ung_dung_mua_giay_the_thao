package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.ReviewApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewRepository @Inject constructor(private val reviewApi: ReviewApi) {
    suspend fun getReview(id: String) = withContext(Dispatchers.IO) {
        reviewApi.getReview(id)
    }
}