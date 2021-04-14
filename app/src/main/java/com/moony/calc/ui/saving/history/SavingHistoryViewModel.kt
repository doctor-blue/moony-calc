package com.moony.calc.ui.saving.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moony.calc.data.SavingHistoryRepository
import com.moony.calc.model.SavingHistory
import kotlinx.coroutines.launch

class SavingHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val savingHistoryRepository: SavingHistoryRepository =
        SavingHistoryRepository(application)

    fun getAllSavingHistory(idSaving: Int) = savingHistoryRepository.getAllSavingHistory(idSaving)

    /**
     * Hàm dưới đây dùng để lấy all số tiền đã tiết kiệm được từ SavingHistory ra
     * lấy được số tiền hiện tại từ đây để tính ra % số tiền đã tiết kiệm được
     */
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

    fun deleteAllSavingHistoryBySaving(idSaving: Int) = viewModelScope.launch {
        savingHistoryRepository.deleteAllSavingHistoryBySaving(idSaving)
    }
}