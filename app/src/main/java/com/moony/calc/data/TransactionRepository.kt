package com.moony.calc.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.moony.calc.database.MoonyDatabase
import com.moony.calc.model.Transaction

class TransactionRepository(application: Application) {
    private var transactionDao =
        MoonyDatabase.getInstance(application).getTransactionDao()

    fun getAllTransaction(): LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    fun getTotalMoney(isIncome: Boolean, month: Int, year: Int): LiveData<Double> =
        transactionDao.getTotalMoney(isIncome, month, year)

    fun getAllTransactionByDate(month: Int, year: Int) = transactionDao.getAllTransactionsByDate(month,year)

    suspend fun insertTransaction(transaction: Transaction) =
        transactionDao.insertTransaction(transaction)

    suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction)

    suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateTransaction(transaction)

    suspend fun deleteAllTransactionByCategory(idCategory: Int) = transactionDao.deleteAllTransactionByCategory(idCategory)
}