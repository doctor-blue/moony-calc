package com.moony.calc.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moony.calc.model.SavingHistory
import kotlinx.coroutines.launch

class SavingHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val savingHistoryRepository: SavingHistoryRepository =
        SavingHistoryRepository(application)

    fun getAllSavingHistory(idSaving: Int) = savingHistoryRepository.getAllSavingHistory(idSaving)

    fun getCurrentAmount(idSaving: Int) = savingHistoryRepository.getCurrentSavingAmount(idSaving)

    fun insertSavingHistory(savingHistory: SavingHistory) = viewModelScope.launch {
        savingHistoryRepository.insertSavingHistory(savingHistory)
    }

    fun updateSavingHistory(savingHistory: SavingHistory) = viewModelScope.launch {
        savingHistoryRepository.updateSavingHistory(savingHistory)
    }

    fun deleteSavingHistory(savingHistory: SavingHistory) = viewModelScope.launch {
        savingHistoryRepository.deleteSavingHistory(savingHistory)
    }
}