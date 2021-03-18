package com.moony.calc.ui.saving

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.moony.calc.data.SavingRepository
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingItem
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

    fun getAllSaving(): LiveData<List<SavingItem>> = savingRepository.getAllSavingGoals()

    fun getSaving(idSaving: Int):LiveData<Saving> = savingRepository.getSaving(idSaving)
}