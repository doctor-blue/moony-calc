package com.moony.calc.data

import androidx.lifecycle.LiveData
import com.moony.calc.database.SavingHistoryDao
import com.moony.calc.model.SavingHistory
import javax.inject.Inject

class SavingHistoryRepository @Inject constructor(private val savingHistoryDao: SavingHistoryDao) {

    suspend fun insertSavingHistory(savingHistory: SavingHistory) =
            savingHistoryDao.insertSavingHistory(savingHistory)

    suspend fun updateSavingHistory(savingHistory: SavingHistory) =
            savingHistoryDao.updateSavingHistory(savingHistory)

    suspend fun deleteSavingHistory(savingHistory: SavingHistory) =
            savingHistoryDao.deleteSavingHistory(savingHistory)

    fun getAllSavingHistory(idSaving: String): LiveData<List<SavingHistory>> =
            savingHistoryDao.getAllSavingHistory(idSaving)

    fun getCurrentSavingAmount(idSaving: String): LiveData<Double> =
            savingHistoryDao.getCurrentSavingAmount(idSaving)


    suspend fun deleteSavingHistoryByTransaction(idTransaction: String) =
            savingHistoryDao.deleteSavingHistoryByTransaction(idTransaction)

    suspend fun deleteAllTransactionBySaving(idSaving: String) =
            savingHistoryDao.deleteAllTransactionBySaving(idSaving)

}