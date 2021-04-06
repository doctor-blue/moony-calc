package com.moony.calc.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moony.calc.model.Saving

@Dao
interface SavingDao {

    @Update
    suspend fun updateSaving(saving: Saving)

    @Delete
    suspend fun deleteSaving(saving: Saving)

    @Insert
    suspend fun insertSaving(saving: Saving)

    @Query("select * from saving_goal")
    fun getAllSavingGoals(): LiveData<List<Saving>>

    @Query("select * from saving_goal where idSaving=:idSaving")
     fun  getSaving(idSaving: Int):LiveData<Saving>

}