package com.moony.calc.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

fun Double.decimalFormat(): String = DecimalFormat("0.00").format(this)

fun Calendar.formatMonth(locale: Locale): String =
    "${getDisplayName(MONTH, LONG, locale)} ${get(YEAR)}"

fun Calendar.formatDateTime(): String = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(time)