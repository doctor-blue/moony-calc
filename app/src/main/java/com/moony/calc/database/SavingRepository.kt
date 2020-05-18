package com.moony.calc.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.moony.calc.model.Saving

class SavingRepository(application: Application) {
    private var savingDao =
        MoonyDatabase.getInstance(application).getSavingDao()

    fun getAllSavingGoals(): LiveData<List<Saving>> =savingDao.getAllSavingGoals()

    suspend fun insertSaving(saving: Saving) = savingDao.insertSaving(saving)

    suspend fun deleteSaving(saving: Saving) = savingDao.deleteSaving(saving)

    suspend fun updateSaving(saving: Saving) = savingDao.updateSaving(saving)
}