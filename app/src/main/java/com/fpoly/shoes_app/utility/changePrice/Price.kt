package com.fpoly.shoes_app.utility.changePrice

import java.text.NumberFormat
import java.util.Locale

object Price {
    fun formatToCurrency(amount: Double): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        return numberFormat.format(amount)
    }
}