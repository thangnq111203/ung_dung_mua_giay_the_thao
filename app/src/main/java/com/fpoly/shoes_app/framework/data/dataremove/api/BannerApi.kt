package com.fpoly.shoes_app.framework.data.dataremove.api

import com.fpoly.shoes_app.framework.domain.model.Banner
import retrofit2.Response
import retrofit2.http.GET

interface BannerApi {

    //get all banner
    @GET("banner-active")
    suspend fun getBanner(): Response<Banner>
}