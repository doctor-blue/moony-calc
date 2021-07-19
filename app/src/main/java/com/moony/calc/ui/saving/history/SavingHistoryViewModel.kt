package com.moony.calc.ui.saving.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moony.calc.data.SavingHistoryRepository
import com.moony.calc.model.SavingHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavingHistoryViewModel @Inject constructor(private val savingHistoryRepository: SavingHistoryRepository) : ViewModel() {

    val savingHistoryList :MutableLiveData<List<SavingHistory>> = MutableLiveData(listOf())


    fun submitList(list: List<SavingHistory>){
        savingHistoryList.value = list
    }

    fun getAllSavingHistory(idSaving: String) = savingHistoryRepository.getAllSavingHistory(idSaving)

    /**
     * Hàm dưới đây dùng để lấy all số tiền đã tiết kiệm được từ SavingHistory ra
     * lấy được số tiền hiện tại từ đây để tính ra % số tiền đã tiết kiệm được
     */
    fun getCurrentAmount(idSaving: String) = savingHistoryRepository.getCurrentSavingAmount(idSaving)

    fun insertSavingHistory(savingHistory: SavingHistory) = viewModelScope.launch {
        savingHistoryRepository.insertSavingHistory(savingHistory)
    }

    fun updateSavingHistory(savingHistory: SavingHistory) = viewModelScope.launch {
        savingHistoryRepository.updateSavingHistory(savingHistory)
    }

    fun deleteSavingHistory(savingHistory: SavingHistory) = viewModelScope.launch {
        savingHistoryRepository.deleteSavingHistory(savingHistory)
    }

    fun deleteAllTransactionBySaving(idSaving: String) = GlobalScope.launch {
        savingHistoryRepository.deleteAllTransactionBySaving(idSaving)
    }

    fun deleteSavingHistoryByTransaction(idTransaction: String) = viewModelScope.launch {
        savingHistoryRepository.deleteSavingHistoryByTransaction(idTransaction)
    }
}