package com.moony.calc.database

import androidx.lifecycle.LiveData
import com.moony.calc.model.DateTime

class DateTimeRepository(private val dateTimeDao: DateTimeDao) {


    fun getAllDateTimeByMonth(month: Int, year: Int): LiveData<List<DateTime>> =
        dateTimeDao.getAllDateTimeByMonth(month, year)

    fun getDateTime(day: Int, month: Int, year: Int): LiveData<DateTime> =
        dateTimeDao.getDateTime(day, month, year)

    suspend fun insertDateTime(dateTime: DateTime) = dateTimeDao.insertDateTime(dateTime)

    suspend fun deleteDateTime(dateTime: DateTime) = dateTimeDao.deleteDateTime(dateTime)
}