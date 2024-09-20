package com.fpoly.shoes_app.framework.data.othetasks

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.fpoly.shoes_app.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File

object CheckValidate {
    fun checkPhone(
        context: Context, edtData: TextInputEditText, layoutData: TextInputLayout,button: Button
    ): Boolean {
        // Check if the phone number length is less than 10 characters
        return if ((edtData.text?.length ?: 0) < 10) {
            // Set an error message if the phone number is invalid
            layoutData.error = context.getString(R.string.is_number_phone)
            // Return false indicating the validation failed
            false
        } else {
            // Clear the error message if the phone number is valid
            layoutData.error = null
            button.requestFocus()
            true
        }
    }


    fun checkEmail(
        context: Context,
        edtData: TextInputEditText,
        layoutData: TextInputLayout,
        layoutDataMail: TextInputLayout
    ): Boolean {
        return if ((edtData.text?.length ?: 0) == 0) {
            layoutData.error = context.getString(R.string.force_input_email)
            false
        } else if (!isValidEmail(edtData.text)) {
            layoutData.error = context.getString(R.string.error_format_email)
            false
        } else {
            layoutData.error = null
            true
            layoutDataMail.requestFocus()
        }
    }

    fun checkStr(
        context: Context,
        edtData: TextInputEditText,
        layoutData: TextInputLayout,
        layoutDataMail: TextInputLayout
    ): Boolean {
        return if ((edtData.text?.length ?: 0) == 0) {
            layoutData.error = context.getString(R.string.inputFullInfo)
            false
        } else {
            layoutData.error = null
            true
            layoutDataMail.requestFocus()
        }
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target!!).matches()
    }

    fun strNullOrEmpty(string: String?): String {
        if (string.isNullOrEmpty()) {
            return ""
        }
        return string
    }
    fun validateInput(context: Context, name: String, mail: String, phone: String, birth: String, gender: String, imagePath: File?): Boolean {
        if (name.isBlank() || mail.isBlank() || phone.isBlank() || birth.isBlank() || gender.isBlank() || imagePath == null) {
            Toast.makeText(context, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            return false
        }
        // Add any other validation logic as needed
        return true
    }
}