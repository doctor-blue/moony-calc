package com.moony.calc.ui.saving

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moony.calc.data.SavingRepository
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavingViewModel @Inject constructor(private val savingRepository: SavingRepository) : ViewModel() {

    val savingList: MutableLiveData<List<Saving>> = MutableLiveData(listOf())

    fun submitList(list: List<Saving>) {
        savingList.value = list
    }

    fun insertSaving(saving: Saving) = viewModelScope.launch {
        savingRepository.insertSaving(saving)
    }

    fun updateSaving(saving: Saving) = viewModelScope.launch {
        savingRepository.updateSaving(saving)
    }

    fun deleteSaving(saving: Saving) = viewModelScope.launch {
        savingRepository.deleteSaving(saving)
    }

    fun getAllSaving(): LiveData<List<Saving>> = savingRepository.getAllSavingGoals()

    fun getSaving(idSaving: String): LiveData<Saving> = savingRepository.getSaving(idSaving)

    fun getAllSavingItem(): LiveData<List<SavingItem>> = savingRepository.getAllSavingItem()
}