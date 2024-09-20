package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.ShoesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShoesRepository @Inject constructor(private val shoesApi: ShoesApi) {
    suspend fun getShoes() = withContext(Dispatchers.IO) {
        shoesApi.getShoes()
    }

    suspend fun getShoeDetail(id: String) = withContext(Dispatchers.IO) {
        shoesApi.getShoeDetail(id)
    }
}