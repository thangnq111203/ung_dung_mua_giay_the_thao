package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.SetUpInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.SignUpInterface
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccount
import com.fpoly.shoes_app.framework.domain.model.signUp.SignUp
import javax.inject.Inject

class EditProfileRepository @Inject constructor(
    private val setUpApi: SetUpInterface
) {
//    suspend fun signUp(id:String,setUp: SetUpAccount) = setUpApi.setUpAccount(id,setUp)
}