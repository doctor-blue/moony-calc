package com.moony.calc.data

import androidx.lifecycle.LiveData
import com.moony.calc.database.TransactionDao
import com.moony.calc.model.ChartItem
import com.moony.calc.model.Transaction
import com.moony.calc.model.TransactionItem
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val transactionDao: TransactionDao) {


    fun getAllTransaction(): LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    fun getTotalMoney(month: Int, year: Int): LiveData<Double> =
        transactionDao.getTotalMoney(month, year)

    fun getAllTransactionByDate(month: Int, year: Int) =
        transactionDao.getAllTransactionsByDate(month, year)

    suspend fun insertTransaction(transaction: Transaction) =
        transactionDao.insertTransaction(transaction)

    suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction)

    suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateTransaction(transaction)

    suspend fun deleteAllTransactionByCategory(idCategory: String) =
        transactionDao.deleteAllTransactionByCategory(idCategory)

    fun getAllTransactionItem(month: Int, year: Int): LiveData<List<TransactionItem>> =
        transactionDao.getAllTransactionItem(month, year)

    fun getChartItem(month: Int, year: Int): LiveData<List<ChartItem>> =
        transactionDao.getChartItem(month, year)
}