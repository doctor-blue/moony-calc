package com.moony.calc.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.moony.calc.model.Transaction

class TransactionRepository(application: Application) {
    private var applicationDao: ApplicationDao =
        MoonyDatabase.getInstance(application)!!.getApplicationDao()
    private lateinit var allTransaction: LiveData<List<Transaction>>

    fun getAllTransaction(): LiveData<List<Transaction>> {
        allTransaction = applicationDao.getAllTransactions()
        return allTransaction
    }

    fun getTotalMoney(isIncome:Boolean,month:Int,year:Int): LiveData<Double> = applicationDao.getTotalMoney(isIncome,month,year)

    fun getAllTransactionByDate(id: Int) = applicationDao.getAllTransactionsByDate(id)

    fun insertTransaction(transaction: Transaction) {
        InsertTask(applicationDao).execute(transaction)
    }

    fun deleteTransaction(transaction: Transaction) {
        DeleteTask(applicationDao).execute(transaction)
    }

    fun updateTransaction(transaction: Transaction) {
        UpdateTask(applicationDao).execute(transaction)
    }

    private class InsertTask(private val applicationDao: ApplicationDao) :
        AsyncTask<Transaction, Unit?, Unit?>() {
        override fun doInBackground(vararg transaction: Transaction): Unit? {
            applicationDao.insertTransaction(transaction[0])
            return null
        }

    }

    private class UpdateTask(private val applicationDao: ApplicationDao) :
        AsyncTask<Transaction, Unit?, Unit?>() {
        override fun doInBackground(vararg transaction: Transaction): Unit? {
            applicationDao.updateTransaction(transaction[0])
            return null
        }

    }

    private class DeleteTask(private val applicationDao: ApplicationDao) :
        AsyncTask<Transaction, Unit?, Unit?>() {
        override fun doInBackground(vararg transaction: Transaction): Unit? {
            applicationDao.deleteTransaction(transaction[0])
            return null
        }


    }
}