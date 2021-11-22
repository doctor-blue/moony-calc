package com.moony.calc.utils

import androidx.room.TypeConverter
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimestampConverter {
    var df: DateFormat = SimpleDateFormat(Constants.TIME_STAMP_FORMAT, Locale.ENGLISH)

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return if (value != null) {
            try {
                return df.parse(value)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            Date()
        } else {
            Date()
        }
    }

    @TypeConverter
    fun dateToTimestamp(value: Date?): String? {
        return if (value == null) "" else df.format(value)
    }
}