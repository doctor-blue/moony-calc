package com.moony.calc.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moony.calc.data.CategoryRepository
import com.moony.calc.model.Category
import com.moony.calc.model.TransactionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) : ViewModel() {

    val incomeCategories: MutableLiveData<List<Category>> = MutableLiveData(listOf())
    val expensesCategories: MutableLiveData<List<Category>> = MutableLiveData(listOf())

    fun submitCategories(categories:List<Category>,isIncome: Boolean){
        if (isIncome){
            incomeCategories.value = categories
        }else{
            expensesCategories.value = categories
        }
    }


    fun insertCategory(category: Category) = viewModelScope.launch {
        categoryRepository.insertCategory(category)
    }

    fun updateCategory(category: Category) = viewModelScope.launch {
        categoryRepository.updateCategory(category)
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        categoryRepository.deleteCategory(category)
    }

    fun getAllCategory(isIncome: Boolean): LiveData<List<Category>> =
        categoryRepository.getAllCategory(isIncome)

    fun getCategory(id: String): LiveData<Category> = categoryRepository.getCategory(id)

    fun getSavingCategory(isIncome: Boolean, resId: Int) =
        categoryRepository.getSavingCategory(isIncome, resId)
}