package com.moony.calc.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.moony.calc.model.Saving

class SavingRepository(application: Application) {
    private var applicationDao: ApplicationDao =
        MoonyDatabase.getInstance(application)!!.getApplicationDao()
    private lateinit var allSaving: LiveData<List<Saving>>

    fun getAllSavingGoals(): LiveData<List<Saving>> {
        allSaving = applicationDao.getAllSavingGoals()
        return allSaving
    }

    fun insertSaving(saving: Saving) {
        InsertTask(applicationDao).execute(saving)
    }

    fun deleteSaving(saving: Saving) {
        DeleteTask(applicationDao).execute(saving)
    }

    fun updateSaving(saving: Saving) {
        InsertTask(applicationDao).execute(saving)
    }

    private class InsertTask(private val applicationDao: ApplicationDao) :
        AsyncTask<Saving, Unit?, Unit?>() {
        override fun doInBackground(vararg saving: Saving): Unit? {
            applicationDao.insertSaving(saving[0])
            return null
        }


    }

    private class UpdateTask(private val applicationDao: ApplicationDao) :
        AsyncTask<Saving, Unit?, Unit?>() {
        override fun doInBackground(vararg saving: Saving): Unit? {
            applicationDao.updateSaving(saving[0])
            return null
        }

    }

    private class DeleteTask(private val applicationDao: ApplicationDao) :
        AsyncTask<Saving, Unit?, Unit?>() {
        override fun doInBackground(vararg saving: Saving): Unit? {
            applicationDao.deleteSaving(saving[0])
            return null
        }
    }
}