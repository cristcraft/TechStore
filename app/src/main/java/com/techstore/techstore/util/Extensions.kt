package com.techstore.techstore.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.NumberFormat
import java.util.Locale

fun View.visible() { visibility = View.VISIBLE }
fun View.gone() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun Fragment.toast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

// Formatea precio en COP: $1.299.900 COP
fun Double.toCOP(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    return format.format(this)
}

fun String.isValidEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
fun String.isValidPhone() = this.startsWith("+57") && this.length == 13
