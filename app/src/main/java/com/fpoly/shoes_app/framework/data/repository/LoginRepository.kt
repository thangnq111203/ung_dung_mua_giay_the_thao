package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.LoginInterface
import com.fpoly.shoes_app.framework.domain.model.login.Login
import com.fpoly.shoes_app.framework.domain.model.login.LoginResponse
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUp
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val apiService: LoginInterface
) {
    suspend fun signIn(signIn: Login) = apiService.signIn(signIn)
}