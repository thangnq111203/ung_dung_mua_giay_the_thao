package com.fpoly.shoes_app.utility.convert_image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File

fun imageToBase64(context: Context, bitmap: Bitmap?): String? {
    try {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    } catch (ex: Exception) {
        Toast.makeText(context, "Lỗi lấy ảnh", Toast.LENGTH_LONG).show()
    }
    return null
}