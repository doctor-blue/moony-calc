package com.moony.calc.data

import androidx.lifecycle.LiveData
import com.moony.calc.database.CategoryDao
import com.moony.calc.model.Category
import javax.inject.Inject

class CategoryRepository @Inject constructor(private var categoryDao: CategoryDao) {

    fun getAllCategory(isIncome: Boolean): LiveData<List<Category>> =categoryDao.getAllCategory(isIncome)

    fun getCategory(id: String): LiveData<Category> = categoryDao.getCategory(id)
    fun getSavingCategory(isIncome: Boolean, resId: Int)=categoryDao.getSavingCategory(isIncome, resId)


    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)

    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)

    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
}