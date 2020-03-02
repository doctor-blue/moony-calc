package com.moony.calc.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moony.calc.model.Category
import com.moony.calc.model.Saving
import com.moony.calc.model.Transaction

@Dao
interface ApplicationDao {
    @Update
    fun updateCategory(category: Category)
    @Delete
    fun deleteCategory(category: Category)
    @Insert
    fun insertCategory(category: Category)
    @Query("select*from Category where isIncome = :isIncome")
    fun getAllCategory(isIncome:Boolean):LiveData<List<Category>>

    @Update
    fun updateSaving(saving: Saving)
    @Delete
    fun  deleteSaving(saving: Saving)
    @Insert
    fun insertSaving(saving: Saving)
    @Query("select*from saving_goal")
    fun getAllSavingGoals():LiveData<List<Saving>>

    @Update
    fun updateTransaction(transaction: Transaction)
    @Delete
    fun deleteTransaction(transaction: Transaction)
    @Insert
    fun insertTransaction(transaction: Transaction)
    @Query("select*from `Transaction`")
    fun getAllTransactions():LiveData<List<Transaction>>
}