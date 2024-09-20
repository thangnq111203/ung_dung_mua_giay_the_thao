package com.fpoly.shoes_app.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.ImageView
import androidx.core.content.ContextCompat
import coil.load
import com.fpoly.shoes_app.R
import java.security.MessageDigest
import java.text.Normalizer
import java.text.NumberFormat
import java.util.Locale


fun ImageView.loadImage(imgResource: Int? = null) {
    this.load(imgResource) {
        placeholder(R.color.primary_white)
        error(R.color.primary_white)
    }
}

fun ImageView.loadImage(imageUrl: String? = null) {
    this.load(imageUrl) {
        placeholder(R.color.primary_white)
        error(R.color.primary_white)
    }
}

fun Context.getBitmapFromDrawable(drawableResId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(this, drawableResId)
    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun Long.formatPriceShoe(): String {
    val number = this
    val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return format.format(number)
}

fun Int.formatSoldShoe(): String {
    return "Đã bán $this"
}

fun Int.formatReviewShoe(): String {
    return "($this bình luận)"
}

fun Int.formatQuantityShoe(): String {
    return "Kho: $this"
}

fun String.toMD5(): String {
    val bytes = this.toByteArray()
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(bytes)
    return digest.joinToString("") { "%02x".format(it) }
}

fun String.formatToVND(): String {
    val number = this.toLongOrNull() ?: return "Invalid number"
    val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return format.format(number)
}
fun Int?.toTotal(price: Long?): Long = this?.times(price ?: 0L) ?: 0L

fun String.toDiscount(): String = "-$this"

fun String.normalizeString(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}".toRegex(), "")

    val noSpaces = normalized.replace("\\s".toRegex(), "")

    return noSpaces.lowercase()
}

fun Int.formatDiscountTitle(): String = "Phiếu giảm giá $this%"

fun String.formatDiscountDescription(): String = "Có hiệu lực đến $this"

fun String.formatTextSearch(): String =
    if (this.isBlank()) "Hiển thị tất cả sản phẩm" else "Kết quả cho '$this'"

fun Int.formatSizeSearch(): String = "$this sản phẩm"

fun Int.formatReview(): String = "Đánh giá ($this lượt)"

fun Int.formatRating(): String = if (this == RatingText.RATING_ALL) "Tất cả" else this.toString()
