package com.moony.calc.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.moony.calc.model.Transaction

class TransactionRepository(application: Application) {
    private var transactionDao =
        MoonyDatabase.getInstance(application).getTransactionDao()

    fun getAllTransaction(): LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    fun getTotalMoney(isIncome: Boolean, month: Int, year: Int): LiveData<Double> =
        transactionDao.getTotalMoney(isIncome, month, year)

    fun getAllTransactionByDate(id: Int) = transactionDao.getAllTransactionsByDate(id)

    suspend fun insertTransaction(transaction: Transaction) =
        transactionDao.insertTransaction(transaction)

    suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction)

    suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateTransaction(transaction)

    suspend fun deleteAllTransactionByCategory(idCategory: Int) = transactionDao.deleteAllTransactionByCategory(idCategory)
}