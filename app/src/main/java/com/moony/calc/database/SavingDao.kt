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

    @Query("select saving_goal.*, category_table.* from saving_goal inner join category_table on saving_goal.idCategory = category_table.idCategory")
    fun getAllSavingGoals(): LiveData<List<SavingItem>>

    @Query("select * from saving_goal where idSaving=:idSaving")
     fun  getSaving(idSaving: Int):LiveData<Saving>

}