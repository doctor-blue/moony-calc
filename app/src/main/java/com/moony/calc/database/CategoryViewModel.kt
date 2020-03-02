package com.moony.calc.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.moony.calc.model.Category

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val categoryRepository: CategoryRepository = CategoryRepository(application)

    fun insertCategory(category: Category) {
        categoryRepository.insertCategory(category)
    }

    fun updateCategory(category: Category) {
        categoryRepository.updateCategory(category)
    }

    fun deleteCategory(category: Category) {
        categoryRepository.deleteCategory(category)
    }

    fun getAllCategory(isIncome:Boolean): LiveData<List<Category>> = categoryRepository.getAllCategory(isIncome)
}