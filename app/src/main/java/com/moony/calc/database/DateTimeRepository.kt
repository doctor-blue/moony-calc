package com.moony.calc.database

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.moony.calc.model.DateTime

class DateTimeRepository(private val applicationDao: ApplicationDao) {


    fun getAllDateTimeByMonth(month: String): LiveData<List<DateTime>> =
        applicationDao.getAllDateTimeByMonth(month)

    fun getDateTime(day: Int, month: String): LiveData<DateTime> =
        applicationDao.getDateTime(day, month)

    fun insertDateTime(dateTime: DateTime) {
        InsertTask(applicationDao).execute(dateTime)
    }

    fun deleteDateTime(dateTime: DateTime) {
        DeleteTask(applicationDao).execute(dateTime)
    }

    private class InsertTask(private val applicationDao: ApplicationDao) :
        AsyncTask<DateTime, Unit?, Unit?>() {
        override fun doInBackground(vararg params: DateTime): Unit? {
            applicationDao.insertDateTime(params[0])
            return null
        }

    }

    private class DeleteTask(private val applicationDao: ApplicationDao) :
        AsyncTask<DateTime, Unit?, Unit?>() {
        override fun doInBackground(vararg params: DateTime): Unit? {
            applicationDao.deleteDateTime(params[0])
            return null
        }

    }
}