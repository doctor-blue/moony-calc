package com.moony.calc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moony.calc.R
import com.moony.calc.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [Transaction::class, Saving::class, Category::class, SavingHistory::class],
    version = 1
)
abstract class MoonyDatabase : RoomDatabase() {
    abstract fun getTransactionDao(): TransactionDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getSavingDao(): SavingDao
    abstract fun getSavingHistoryDao(): SavingHistoryDao

    companion object {
        @Volatile
        private var instance: MoonyDatabase? = null
        fun getInstance(context: Context): MoonyDatabase {
            if (instance == null)
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MoonyDatabase::class.java,
                        "MoonyDatabase.db"
                    ).addCallback(callback).build()
                }
            return instance!!
        }

        private val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
//                adDefaultCategory(instance!!.getCategoryDao())
            }
        }

//        private fun adDefaultCategory(categoryDao: CategoryDao) {
//            GlobalScope.launch(Dispatchers.IO) {
//            }
//        }

    }

}