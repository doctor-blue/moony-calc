package com.moony.calc.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moony.calc.model.Transaction
import com.moony.calc.model.TransactionItem

@Dao
interface TransactionDao {

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Query("select*from transaction_table")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Query("select*from transaction_table  where month=:month and year=:year ")
    fun getAllTransactionsByDate(month: Int, year: Int): LiveData<List<Transaction>>

    @Query("select sum(money) as double from transaction_table where isIncome =:isIncome and month=:month and year=:year")
    fun getTotalMoney(isIncome: Boolean, month: Int, year: Int): LiveData<Double>

    @Query("delete from transaction_table where idCategory=:idCategory")
    suspend fun deleteAllTransactionByCategory(idCategory: Int)

    @Query("select transaction_table.*, category_table.* from transaction_table inner join category_table on transaction_table.idCategory = category_table.idCategory ")
    fun getAllTransactionItem(): LiveData<List<TransactionItem>>

}