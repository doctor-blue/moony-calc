package com.moony.calc.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.moony.calc.model.Saving

class SavingViewModel(application: Application) : AndroidViewModel(application) {
    private val savingRepository: SavingRepository = SavingRepository(application)

    fun insertSaving(saving: Saving) {
        savingRepository.insertSaving(saving)
    }

    fun updateSaving(saving: Saving) {
        savingRepository.updateSaving(saving)
    }

    fun deleteSaving(saving: Saving) {
        savingRepository.deleteSaving(saving)
    }

    fun getAllSaving(): LiveData<List<Saving>> = savingRepository.getAllSavingGoals()
}