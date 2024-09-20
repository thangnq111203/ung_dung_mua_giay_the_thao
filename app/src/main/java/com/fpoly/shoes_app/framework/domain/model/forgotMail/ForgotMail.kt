package com.fpoly.shoes_app.framework.domain.model.forgotMail

import android.os.Parcelable
import com.fpoly.shoes_app.framework.domain.model.baseError.BaseErrResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForgotMail(
    @SerializedName("nameAccount") val nameAccount: String,
): Parcelable

 class ForgotMailResponse(
    @SerializedName("userId") val userId: String?,
     success: Boolean,
     message: String?
) : BaseErrResponse(success, message)