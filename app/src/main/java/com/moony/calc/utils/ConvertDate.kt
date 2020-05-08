package com.moony.calc.utils

import java.text.SimpleDateFormat
import java.util.*

object ConvertDate {
    private val dateFormat=SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH)
    fun formatMonth(calendar:Calendar,locale: Locale):String= "${calendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            locale
        )} ${calendar.get(
            Calendar.YEAR
        )}"

    fun formatDateTime(calendar: Calendar):String= dateFormat.format(calendar.time)
}