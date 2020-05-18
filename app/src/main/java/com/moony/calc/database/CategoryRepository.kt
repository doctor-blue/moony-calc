package com.moony.calc.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.moony.calc.model.Category

class CategoryRepository(application: Application) {
    private var categoryDao: CategoryDao =
        MoonyDatabase.getInstance(application).getCategoryDao()


    fun getAllCategory(isIncome: Boolean): LiveData<List<Category>> =categoryDao.getAllCategory(isIncome)

    fun getCategory(id: Int): LiveData<Category> = categoryDao.getCategory(id)


    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)

    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)

    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
}