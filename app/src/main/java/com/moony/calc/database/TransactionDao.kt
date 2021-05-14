package com.moony.calc.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moony.calc.model.ChartItem
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

    @Query("select*from transaction_table  where  strftime('%Y', transactionTime) = :year and strftime('%m', transactionTime) = :month")
    fun getAllTransactionsByDate(month: Int, year: Int): LiveData<List<Transaction>>

    @Query("select sum(money) as double from transaction_table where  CAST(strftime('%Y', transactionTime) as INTEGER) = :year and  CAST(strftime('%m', transactionTime) as INTEGER)=:month+1")
    fun getTotalMoney(month: Int, year: Int): LiveData<Double>

    @Query("delete from transaction_table where idCategory=:idCategory")
    suspend fun deleteAllTransactionByCategory(idCategory: String)

    @Query("select transaction_table.*, category_table.* from transaction_table inner join category_table on transaction_table.idCategory = category_table.idCategory where   CAST(strftime('%Y', transactionTime) as INTEGER) = :year and  CAST(strftime('%m', transactionTime) as INTEGER)=:month+1")
    fun getAllTransactionItem(month: Int, year: Int): LiveData<List<TransactionItem>>

    @Query("select transaction_table.*, category_table.*, sum(transaction_table.money) as 'sum' from transaction_table inner join category_table on transaction_table.idCategory = category_table.idCategory where  CAST(strftime('%Y', transactionTime) as INTEGER) = :year and  CAST(strftime('%m', transactionTime) as INTEGER)=:month+1 group by category_table.idCategory")
    fun getChartItem(month: Int, year: Int): LiveData<List<ChartItem>>
}