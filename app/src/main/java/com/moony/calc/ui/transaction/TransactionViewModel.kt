package com.moony.calc.ui.transaction

import androidx.lifecycle.*
import com.moony.calc.data.TransactionRepository
import com.moony.calc.model.Transaction
import com.moony.calc.model.TransactionItem
import com.moony.calc.utils.decimalFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val transactionRepository: TransactionRepository) :
    ViewModel() {

    private var _totalIncome = 0.0
    private var _totalExpenses = 0.0

    val isLoading = MutableLiveData(false)
    val transactions: MutableLiveData<List<TransactionItem>> = MutableLiveData(listOf())

    val totalIncome = MutableLiveData("0")
    val totalExpenses = MutableLiveData("0")
    val totalBalance = MutableLiveData("0")

    fun setLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    fun submitList(list: List<TransactionItem>) {
        transactions.value = list

        _totalIncome = 0.0
        _totalExpenses = 0.0
        viewModelScope.launch(Dispatchers.Default) {
            list.asFlow().collect {
                if (it.category.isIncome) {
                    _totalIncome += it.transaction.money
                } else {
                    _totalExpenses += it.transaction.money
                }
                withContext(Dispatchers.Main) {
                    totalIncome.value = _totalIncome.decimalFormat()
                    totalExpenses.value = _totalExpenses.decimalFormat()
                    totalBalance.value = (_totalIncome - _totalExpenses).decimalFormat()
                }
            }
        }

        if (list.isEmpty()) {
            totalIncome.value = "0"
            totalExpenses.value = "0"
            totalBalance.value = "0"
        }

        setLoading(false)
    }

    fun insertTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepository.insertTransaction(transaction)
    }


    fun updateTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepository.updateTransaction(transaction)
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepository.deleteTransaction(transaction)
    }


    fun deleteAllTransactionByCategory(idCategory: String) = viewModelScope.launch {
        transactionRepository.deleteAllTransactionByCategory(idCategory)
    }

    fun getAllTransactionItem(month: Int, year: Int) =
        transactionRepository.getAllTransactionItem(month, year)

    fun getChartData(month: Int, year: Int) = transactionRepository.getChartItem(month, year)


//    fun getAllTransaction(): LiveData<List<Transaction>> = transactionRepository.getAllTransaction()
//
//    fun getAllTransactionByDate(month: Int, year: Int): LiveData<List<Transaction>> =
//        transactionRepository.getAllTransactionByDate(month, year)
//
//    fun getTotalMoney(month: Int, year: Int): LiveData<Double> =
//        transactionRepository.getTotalMoney(month, year)

}