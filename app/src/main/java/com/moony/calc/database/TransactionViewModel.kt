package com.moony.calc.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.moony.calc.model.Transaction
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val transactionRepository: TransactionRepository = TransactionRepository(application)

    fun insertTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepository.insertTransaction(transaction)
    }

    fun updateTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepository.updateTransaction(transaction)
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepository.deleteTransaction(transaction)
    }

    fun getAllTransaction(): LiveData<List<Transaction>> = transactionRepository.getAllTransaction()

    fun getAllTransactionByDate(id: Int): LiveData<List<Transaction>> =
        transactionRepository.getAllTransactionByDate(id)

    fun getTotalMoney(isIncome: Boolean, month: Int, year: Int): LiveData<Double> =
        transactionRepository.getTotalMoney(isIncome, month, year)

}