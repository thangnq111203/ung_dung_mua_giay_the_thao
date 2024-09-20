package com.fpoly.shoes_app.framework.data.othetasks

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fpoly.shoes_app.R
import java.io.IOException
import java.io.InputStream
import kotlin.io.encoding.Base64

@SuppressLint("StaticFieldLeak", "InflateParams")
object AddImage {

    private var activity: Activity? = null
    private var fragment: Fragment? = null
    var imageUri: Uri? = null
    private var onImagePicked: ((Intent) -> Unit)? = null

    fun Glides(unit: Any, context: Context, image: ImageView) {
        Glide.with(context)
            .load(unit)
            .placeholder(R.drawable.baseline_account_circle_24)
            .error(R.drawable.baseline_account_circle_24)
            .circleCrop()
            .into(image)
    }

    fun openImageDialog(
        uriPath: String?,
        context: Context,
        activity: Activity,
        onOptionSelected: (Intent) -> Unit
    ) {
        val options =
            arrayOf(context.getString(R.string.capture), context.getString(R.string.gallery))
        val dialogView = activity.layoutInflater.inflate(R.layout.dialog_image_preview, null)
        val imageView: ImageView = dialogView.findViewById(R.id.image_preview)
        var bmp : Bitmap?=null
        if (uriPath?.isNotEmpty() == true){
            val decodeDataImg = android.util.Base64.decode(uriPath, android.util.Base64.DEFAULT)
            bmp = BitmapFactory.decodeByteArray(decodeDataImg, 0, decodeDataImg.size)
        }
            Glide.with(context)
                .load(bmp)
                .placeholder(R.drawable.baseline_account_circle_24)
                .error(R.drawable.baseline_account_circle_24)
                .into(imageView)


//        uriPath?.let {
//            Glide.with(context)
//                .load(it)
//                .placeholder(R.drawable.baseline_account_circle_24)
//                .error(R.drawable.baseline_account_circle_24)
//                .override(800, 800) // Set image size to 800x800
//                .into(imageView)
//        }

        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.selectImg))
            .setView(dialogView) // Set the custom view
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        // Take photo
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        imageUri = getUriForImage(context)  // Create and return a Uri for the image
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        onOptionSelected(takePictureIntent)
                    }

                    1 -> {
                        // Choose from gallery
                        val pickPhotoIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        onOptionSelected(pickPhotoIntent)
                    }
                }
            }
            .show()
    }

    private fun takePicture(context: Context) {
        try {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            imageUri = getUriForImage(context)
            if (imageUri != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                onImagePicked?.invoke(takePictureIntent)
            }
        } catch (ex: Exception) {
            println("" + ex)
        }
    }

    private fun getUriForImage(context: Context): Uri? {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    "temp_image_${System.currentTimeMillis()}.jpg"
                )
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/")
            }
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        } catch (ex: Exception) {
            println("Error creating image URI: ${ex.message}")
            null
        }
    }

    private fun chooseFromGallery() {
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        onImagePicked?.invoke(pickPhotoIntent)
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    }

    fun rotateImageIfRequired(context: Context, imgUri: Uri): Bitmap? {
        var inputStream: InputStream? = null
        return try {
            inputStream = context.contentResolver.openInputStream(imgUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val exif = ExifInterface(context.contentResolver.openInputStream(imgUri)!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flipImage(bitmap, horizontal = true)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> flipImage(bitmap, horizontal = false)
                else -> bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }

    private fun flipImage(img: Bitmap, horizontal: Boolean): Bitmap {
        val matrix = Matrix()
        if (horizontal) {
            matrix.preScale(-1.0f, 1.0f)
        } else {
            matrix.preScale(1.0f, -1.0f)
        }
        return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    }

    fun handleImageSelectionResult(data: Intent?): Uri? {
        return data?.data
    }
}