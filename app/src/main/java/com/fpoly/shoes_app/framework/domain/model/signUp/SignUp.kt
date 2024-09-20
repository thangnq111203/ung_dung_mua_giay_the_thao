package com.fpoly.shoes_app.framework.domain.model.signUp

import android.os.Parcelable
import com.fpoly.shoes_app.framework.domain.model.baseError.BaseErrResponse
import com.fpoly.shoes_app.framework.domain.model.user.User
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUp(
    @SerializedName("nameAccount") val nameAccount: String,
    @SerializedName("namePassword") val namePassword: String,
): Parcelable
 class SignUpResponse(
     @SerializedName("user") val user: User?,
     success: Boolean,
     message: String?
) : BaseErrResponse(success, message)
