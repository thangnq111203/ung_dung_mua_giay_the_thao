package com.fpoly.shoes_app.framework.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.SetUpInterface
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccountResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class SetUpAccountRepository @Inject constructor(
    private val setUpApi: SetUpInterface
) {
    suspend fun setUpAccount(
        id: String,
        imageFile: File?,
        phoneNumber: String?,
        fullName: String?,
        nameAccount: String?,
        birthday: String?,
        grender: String?
    ): Response<SetUpAccountResponse> {
        // Tạo MultipartBody.Part cho ảnh
        val imagePart = imageFile?.let {
            val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imageAccount", it.name, requestFile)
        }

        // Tạo RequestBody cho các trường văn bản
        val phoneNumberBody = phoneNumber?.toRequestBody("text/plain".toMediaTypeOrNull())
        val fullNameBody = fullName?.toRequestBody("text/plain".toMediaTypeOrNull())
        val nameAccountBody = nameAccount?.toRequestBody("text/plain".toMediaTypeOrNull())
        val birthdayBody = birthday?.toRequestBody("text/plain".toMediaTypeOrNull())
        val grenderBody = grender?.toRequestBody("text/plain".toMediaTypeOrNull())

        return setUpApi.setUpAccount(
            id,
            imagePart,
            phoneNumberBody,
            fullNameBody,
            nameAccountBody,
            birthdayBody,
            grenderBody
        )
    }
}