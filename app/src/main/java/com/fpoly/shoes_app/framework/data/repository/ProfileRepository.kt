package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.ProfileInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.OTPConfirmInterface
import com.fpoly.shoes_app.framework.domain.model.otpConfirm.OtpConfirm
import javax.inject.Inject

class ProfileRepository
@Inject constructor(
    private val apiService: ProfileInterface
) {
    suspend fun profile(id: String) = apiService.profile(id)
}