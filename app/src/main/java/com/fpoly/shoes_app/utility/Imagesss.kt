package com.fpoly.shoes_app.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment

object Imagesss {
        private const val PICK_IMAGE_REQUEST_CODE = 100

        fun openImageDialog(context: Context, activity: Activity, fragment: Fragment) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            fragment.startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }

        fun handleImageSelectionResult(requestCode: Int, resultCode: Int, data: Intent?): Uri? {
            if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                return data?.data
            }
            return null
        }

        fun getPathFromUri(uri: Uri, context: Context): String {
            var path = ""
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    path = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                }
            }
            return path
        }

}