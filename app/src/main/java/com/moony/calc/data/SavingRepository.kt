package com.moony.calc.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.moony.calc.database.MoonyDatabase
import com.moony.calc.model.Saving

class SavingRepository(application: Application) {
    private var savingDao =
        MoonyDatabase.getInstance(application).getSavingDao()

    fun getAllSavingGoals(): LiveData<List<Saving>> =savingDao.getAllSavingGoals()

    suspend fun insertSaving(saving: Saving) = savingDao.insertSaving(saving)

    suspend fun deleteSaving(saving: Saving) = savingDao.deleteSaving(saving)

    suspend fun updateSaving(saving: Saving) = savingDao.updateSaving(saving)

     fun  getSaving(idSaving: String):LiveData<Saving> = savingDao.getSaving(idSaving)
}