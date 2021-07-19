package com.moony.calc.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.moony.calc.database.MoonyDatabase
import com.moony.calc.database.SavingDao
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingItem
import javax.inject.Inject

class SavingRepository @Inject constructor( private var savingDao:SavingDao)  {

    fun getAllSavingGoals(): LiveData<List<Saving>> = savingDao.getAllSavingGoals()

    suspend fun insertSaving(saving: Saving) = savingDao.insertSaving(saving)

    suspend fun deleteSaving(saving: Saving) = savingDao.deleteSaving(saving)

    suspend fun updateSaving(saving: Saving) = savingDao.updateSaving(saving)

    fun getSaving(idSaving: String): LiveData<Saving> = savingDao.getSaving(idSaving)

    fun getAllSavingItem(): LiveData<List<SavingItem>> = savingDao.getAllSavingItem()

}