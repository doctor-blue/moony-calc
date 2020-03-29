package com.moony.calc.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.moony.calc.model.DateTime

class DateTimeViewModel(application: Application) : AndroidViewModel(application) {

    private  var dateTimeRepository:DateTimeRepository
    init {
        val applicationDao=MoonyDatabase.getInstance(application)!!.getApplicationDao()
        dateTimeRepository=DateTimeRepository(applicationDao)
    }

    fun getAllDateTimeByMonth(month: String): LiveData<List<DateTime>> =
        dateTimeRepository.getAllDateTimeByMonth(month)

    fun insertDateTime(dateTime: DateTime) {
        dateTimeRepository.insertDateTime(dateTime)
    }

    fun deleteDateTime(dateTime: DateTime) {
        dateTimeRepository.deleteDateTime(dateTime)
    }

}