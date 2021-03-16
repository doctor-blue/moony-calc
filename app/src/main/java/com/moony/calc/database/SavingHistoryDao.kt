package com.moony.calc.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moony.calc.model.SavingHistory
import com.moony.calc.model.SavingHistoryItem

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

    @Query("select saving_history_table.*, category_table.* from saving_history_table inner join category_table on saving_history_table.idCategory = category_table.idCategory where idSaving =:idSaving ")
    fun getAllSavingHistoryItem(idSaving: Int): LiveData<List<SavingHistoryItem>>

}