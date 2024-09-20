package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.BannerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BannerRepository @Inject constructor(private val bannerApi: BannerApi) {
    suspend fun getBanner() = withContext(Dispatchers.IO) {
        bannerApi.getBanner()
    }
}