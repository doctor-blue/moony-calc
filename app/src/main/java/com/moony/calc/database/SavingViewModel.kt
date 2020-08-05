package com.moony.calc.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.moony.calc.model.Saving
import kotlinx.coroutines.launch

class SavingViewModel(application: Application) : AndroidViewModel(application) {
    private val savingRepository: SavingRepository = SavingRepository(application)

    fun insertSaving(saving: Saving) = viewModelScope.launch {
        savingRepository.insertSaving(saving)
    }

    fun updateSaving(saving: Saving) =viewModelScope.launch {
        savingRepository.updateSaving(saving)
    }

    fun deleteSaving(saving: Saving)=viewModelScope.launch {
        savingRepository.deleteSaving(saving)
    }

    fun getAllSaving(): LiveData<List<Saving>> = savingRepository.getAllSavingGoals()

    fun getSaving(idSaving: Int):LiveData<Saving> = savingRepository.getSaving(idSaving)
}