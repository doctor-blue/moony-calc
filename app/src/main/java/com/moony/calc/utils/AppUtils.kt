package com.moony.calc.utils

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*


fun Double.decimalFormat(): String =
    if (this == this.toInt().toDouble()) this.toInt().toString() else DecimalFormat("0.00").format(
        this
    )

fun Calendar.formatMonth(locale: Locale): String =
    "${getDisplayName(MONTH, LONG, locale)} ${get(YEAR)}"


fun Calendar.formatDateTime(): String = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(time)

fun EditText.setAutoHideKeyboard() {
    this.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (!hasFocus) {
            val inputMethodManager: InputMethodManager =
                context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        }

    }
}