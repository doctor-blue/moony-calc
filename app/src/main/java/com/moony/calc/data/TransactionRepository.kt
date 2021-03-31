package com.moony.calc.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.moony.calc.database.MoonyDatabase
import com.moony.calc.model.ChartItem
import com.moony.calc.model.Transaction
import com.moony.calc.model.TransactionItem

class TransactionRepository(application: Application) {
    private var transactionDao =
        MoonyDatabase.getInstance(application).getTransactionDao()

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

    suspend fun deleteAllTransactionByCategory(idCategory: Int) =
        transactionDao.deleteAllTransactionByCategory(idCategory)

    fun getAllTransactionItem(month: Int, year: Int): LiveData<List<TransactionItem>> =
        transactionDao.getAllTransactionItem(month, year)

    fun getChartItem(month: Int, year: Int): LiveData<List<ChartItem>> =
        transactionDao.getChartItem(month, year)
}