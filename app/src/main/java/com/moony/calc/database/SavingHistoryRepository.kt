package com.moony.calc.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.moony.calc.model.SavingHistory

class SavingHistoryRepository(application: Application) {
    private val savingHistoryDao = MoonyDatabase.getInstance(application).getSavingHistoryDao()

    suspend fun insertSavingHistory(savingHistory: SavingHistory) =
        savingHistoryDao.insertSavingHistory(savingHistory)

    suspend fun updateSavingHistory(savingHistory: SavingHistory) =
        savingHistoryDao.updateSavingHistory(savingHistory)

    suspend fun deleteSavingHistory(savingHistory: SavingHistory) =
        savingHistoryDao.deleteSavingHistory(savingHistory)

    fun getAllSavingHistory(idSaving: Int): LiveData<List<SavingHistory>> =
        savingHistoryDao.getAllSavingHistory(idSaving)

    fun getCurrentSavingAmount(idSaving: Int): LiveData<Double> =
        savingHistoryDao.getCurrentSavingAmount(idSaving)


}