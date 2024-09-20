package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.CategoriesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoriesRepository @Inject constructor(private val categoriesApi: CategoriesApi) {
    suspend fun getCategories() = withContext(Dispatchers.IO) {
        categoriesApi.getCategories()
    }
}