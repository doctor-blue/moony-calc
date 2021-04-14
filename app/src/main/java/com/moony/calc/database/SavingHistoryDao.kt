package com.moony.calc.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moony.calc.model.SavingHistory

@Dao
interface SavingHistoryDao {

    @Insert
    suspend fun insertSavingHistory(savingHistory: SavingHistory)

    @Update
    suspend fun updateSavingHistory(savingHistory: SavingHistory)

    @Delete
    suspend fun deleteSavingHistory(savingHistory: SavingHistory)

    @Query("select * from saving_history_table where idSaving=:idSaving")
    fun getAllSavingHistory(idSaving: Int): LiveData<List<SavingHistory>>

    @Query("select sum(amount) from saving_history_table where idSaving=:idSaving ")
    fun getCurrentSavingAmount(idSaving: Int): LiveData<Double>

    @Query("delete from saving_history_table where idSaving=:idSaving")
    suspend fun deleteAllSavingHistoryBySaving(idSaving: Int)

}