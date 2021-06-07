package com.moony.calc.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingItem

@Dao
interface SavingDao {

    @Update
    suspend fun updateSaving(saving: Saving)

    @Delete
    suspend fun deleteSaving(saving: Saving)

    @Insert
    suspend fun insertSaving(saving: Saving)

    @Query("select * from saving_goal_table")
    fun getAllSavingGoals(): LiveData<List<Saving>>

    @Query("select * from saving_goal_table where idSaving=:idSaving")
    fun getSaving(idSaving: String): LiveData<Saving>

    @Query("select saving_goal_table.*, sum(saving_history_table.amount) as 'sum' from saving_goal_table inner join saving_history_table on saving_goal_table.idSaving = saving_history_table.idSaving group by saving_goal_table.idSaving")
    fun getAllSavingItem(): LiveData<List<SavingItem>>

}