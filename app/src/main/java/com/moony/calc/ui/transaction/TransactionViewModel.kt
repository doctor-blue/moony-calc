package com.moony.calc.ui.transaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.moony.calc.data.TransactionRepository
import com.moony.calc.model.Transaction
import kotlinx.coroutines.flow.flow
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

    fun getAllTransactionByDate(month: Int, year: Int): LiveData<List<Transaction>> =
        transactionRepository.getAllTransactionByDate(month, year)

    fun getTotalMoney(month: Int, year: Int): LiveData<Double> =
        transactionRepository.getTotalMoney(month, year)

    fun deleteAllTransactionByCategory(idCategory: String) = viewModelScope.launch {
        transactionRepository.deleteAllTransactionByCategory(idCategory)
    }

    fun getAllTransactionItem(month: Int, year: Int) =
        transactionRepository.getAllTransactionItem(month, year)

    fun getChartData(month: Int, year: Int) = transactionRepository.getChartItem(month, year)

}