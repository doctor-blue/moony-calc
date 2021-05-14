package com.moony.calc.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.moony.calc.data.CategoryRepository
import com.moony.calc.model.Category
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val categoryRepository: CategoryRepository = CategoryRepository(application)

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