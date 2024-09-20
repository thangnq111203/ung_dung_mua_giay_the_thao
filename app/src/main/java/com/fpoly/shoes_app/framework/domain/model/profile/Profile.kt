package com.fpoly.shoes_app.framework.domain.model.profile

import com.fpoly.shoes_app.framework.domain.model.baseError.BaseErrResponse
import com.fpoly.shoes_app.framework.domain.model.user.User
import com.google.gson.annotations.SerializedName

class ProfileResponse(
    @SerializedName("user") val user: User?,
    success: Boolean,
    message: String?
) : BaseErrResponse(success, message)
