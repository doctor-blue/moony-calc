package com.moony.calc.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.moony.calc.model.DateTime

@Dao
interface DateTimeDao {
    @Delete
    suspend fun deleteDateTime(dateTime: DateTime)

    @Insert
    suspend fun insertDateTime(dateTime: DateTime)

    @Query("select*from date_table where month=:month and year=:year")
    fun getAllDateTimeByMonth(month: Int,year:Int): LiveData<List<DateTime>>

    @Query("select*from date_table where month=:month and day=:day and year=:year")
    fun getDateTime(day: Int, month: Int,year:Int): LiveData<DateTime>
}