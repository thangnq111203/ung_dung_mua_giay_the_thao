package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.ForgotMailInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.OTPConfirmInterface
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMail
import com.fpoly.shoes_app.framework.domain.model.otpConfirm.OtpConfirm
import com.fpoly.shoes_app.framework.domain.model.otpConfirm.OtpConfirmResponse
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class OTPConfirmReponsitory
@Inject constructor(
private val apiService: OTPConfirmInterface
) {
    suspend fun otpConfirm(otpConfirm: OtpConfirm) = apiService.OTPConfirm(otpConfirm)
}