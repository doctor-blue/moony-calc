package com.moony.calc.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.moony.calc.database.CategoryDao
import com.moony.calc.database.MoonyDatabase
import com.moony.calc.model.Category

class CategoryRepository(application: Application) {
    private var categoryDao: CategoryDao =
        MoonyDatabase.getInstance(application).getCategoryDao()


    fun getAllCategory(isIncome: Boolean): LiveData<List<Category>> =categoryDao.getAllCategory(isIncome)

    fun getCategory(id: String): LiveData<Category> = categoryDao.getCategory(id)
    fun getSavingCategory(isIncome: Boolean, resId: Int)=categoryDao.getSavingCategory(isIncome, resId)


    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)

    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)

    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
}