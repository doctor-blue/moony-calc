package com.moony.calc.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.moony.calc.model.Category

class CategoryRepository(application: Application) {
    private var applicationDao: ApplicationDao =
        MoonyDatabase.getInstance(application).getApplicationDao()
    private lateinit var allCategory: LiveData<List<Category>>

    fun getAllCategory(isIncome: Boolean): LiveData<List<Category>> {
        allCategory = applicationDao.getAllCategory(isIncome)
        return allCategory
    }

    fun insertCategory(category: Category) {
        InsertTask(applicationDao).execute(category)
    }

    fun deleteCategory(category: Category) {
        DeleteTask(applicationDao).execute(category)
    }

    fun updateCategory(category: Category) {
        UpdateTask(applicationDao).execute(category)
    }

    private class InsertTask(private val applicationDao: ApplicationDao) :
        AsyncTask<Category, Unit?, Unit?>() {
        override fun doInBackground(vararg category: Category): Unit? {
            applicationDao.insertCategory(category[0])
            return null
        }
    }

    private class UpdateTask(private val applicationDao: ApplicationDao) :
        AsyncTask<Category, Unit?, Unit?>() {
        override fun doInBackground(vararg category: Category): Unit? {
            applicationDao.updateCategory(category[0])
            return null
        }
    }

    private class DeleteTask(private val applicationDao: ApplicationDao) :
        AsyncTask<Category, Unit?, Unit?>() {
        override fun doInBackground(vararg category: Category): Unit? {
            applicationDao.deleteCategory(category[0])
            return null
        }

    }
}