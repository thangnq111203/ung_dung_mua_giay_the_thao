package com.fpoly.shoes_app.framework.data.repository

import android.util.Log
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.ForgotMailInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.LoginInterface
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMail
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMailResponse
import com.fpoly.shoes_app.framework.domain.model.login.Login
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class ForgotMailRepository @Inject constructor(
    private val apiService: ForgotMailInterface
) {
    suspend fun forgotMail(forgotMail: ForgotMail) = apiService.forgotMail(forgotMail)
}