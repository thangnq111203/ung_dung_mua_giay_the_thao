package com.fpoly.shoes_app.framework.presentation.ui.forgot

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.fpoly.shoes_app.R

class CustomDialogFragment : DialogFragment() {
    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_custom, null)
        val textView: TextView = dialogView.findViewById(R.id.textView3)
        val text = getString(R.string.txt2_custom_dialog)
        animateEllipsis(textView, text)
        builder.setView(dialogView)
        return builder.create()
    }

    @SuppressLint("SetTextI18n")
    private fun animateEllipsis(textView: TextView, text: String) {
        val animator = ValueAnimator.ofInt(0, 3)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.duration = 500 // Adjust speed here
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { valueAnimator ->
            val ellipsisCount = valueAnimator.animatedValue as Int
            val ellipsisText = ".".repeat(ellipsisCount)
            textView.text = text + ellipsisText
        }

        animator.start()
    }

}