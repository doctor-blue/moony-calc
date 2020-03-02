package com.moony.calc.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.moony.calc.model.Transaction

class TransactionViewModel(application: Application):AndroidViewModel(application) {
    private val transactionRepository:TransactionRepository= TransactionRepository(application)

    fun insertTransaction(transaction: Transaction){
        transactionRepository.insertTransaction(transaction)
    }
    fun updateTransaction(transaction: Transaction){
        transactionRepository.updateTransaction(transaction)
    }
    fun deleteTransaction(transaction: Transaction){
        transactionRepository.deleteTransaction(transaction)
    }
    fun getAllTransaction():LiveData<List<Transaction>> = transactionRepository.getAllTransaction()
}