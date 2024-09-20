package com.fpoly.shoes_app.framework.domain.model.baseError

import com.google.gson.annotations.SerializedName

open class BaseErrResponse(
    @SerializedName("success") open val success: Boolean,
    @SerializedName("message") open val message: String?,
)