package com.fpoly.shoes_app.framework.domain.model.newPass

import android.os.Parcelable
import com.fpoly.shoes_app.framework.domain.model.baseError.BaseErrResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewPass(
    @SerializedName("userId") val userId: String,
    @SerializedName("newPassword") val newPassword: String
): Parcelable


class NewPassResponse(
    success: Boolean,
    message: String?
) : BaseErrResponse(success, message)