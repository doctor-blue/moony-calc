package com.moony.calc.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.moony.calc.model.DateTime
import kotlinx.coroutines.launch

class DateTimeViewModel(application: Application) : AndroidViewModel(application) {

    private var dateTimeRepository: DateTimeRepository

    init {
        val dateTimeDao = MoonyDatabase.getInstance(application).getDateTimeDao()
        dateTimeRepository = DateTimeRepository(dateTimeDao)
    }

    fun getAllDateTimeByMonth(month: Int, year: Int): LiveData<List<DateTime>> =
        dateTimeRepository.getAllDateTimeByMonth(month, year)

    fun insertDateTime(dateTime: DateTime) = viewModelScope.launch {
        dateTimeRepository.insertDateTime(dateTime)
    }


    fun deleteDateTime(dateTime: DateTime) = viewModelScope.launch {
        dateTimeRepository.deleteDateTime(dateTime)
    }

    fun getDateTime(day: Int, month: Int, year: Int) =
        dateTimeRepository.getDateTime(day, month, year)


}