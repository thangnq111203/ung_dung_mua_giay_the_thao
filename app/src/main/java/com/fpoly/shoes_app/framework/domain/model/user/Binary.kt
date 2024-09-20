package com.fpoly.shoes_app.framework.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Binary(
    val base64: String,
    val subType: String
) : Parcelable