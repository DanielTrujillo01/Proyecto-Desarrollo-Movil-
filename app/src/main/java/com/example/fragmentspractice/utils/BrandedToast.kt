package com.example.fragmentspractice.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.example.fragmentspractice.R

/**
 * Toast with the app logo next to the message, used across the app instead
 * of the plain system Toast so every in-app popup carries the brand.
 */
@SuppressLint("InflateParams")
object BrandedToast {

    fun show(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_branded, null)
        view.findViewById<TextView>(R.id.tvToastMessage).text = message

        @Suppress("DEPRECATION")
        Toast(context).apply {
            this.duration = duration
            this.view = view
        }.show()
    }

    fun show(context: Context, @StringRes messageResId: Int, duration: Int = Toast.LENGTH_SHORT) {
        show(context, context.getString(messageResId), duration)
    }
}
