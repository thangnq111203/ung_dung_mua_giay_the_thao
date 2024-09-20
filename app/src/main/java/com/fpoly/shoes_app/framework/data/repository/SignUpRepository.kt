package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.LoginInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.SignUpInterface
import com.fpoly.shoes_app.framework.domain.model.login.Login
import com.fpoly.shoes_app.framework.domain.model.login.LoginResponse
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUp
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUpResponse
import com.fpoly.shoes_app.utility.Resource
import javax.inject.Inject
class SignUpRepository @Inject constructor(
    private val signUpApi: SignUpInterface
) {
    suspend fun signUp(signUp: SignUp) = signUpApi.signUp(signUp)
}