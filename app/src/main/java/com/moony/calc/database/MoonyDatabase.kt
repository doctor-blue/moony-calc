package com.moony.calc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moony.calc.model.*

@Database(
    entities = [Transaction::class, Saving::class, Category::class, DateTime::class, SavingHistory::class],
    version = 1
)
abstract class MoonyDatabase : RoomDatabase() {
    abstract fun getTransactionDao(): TransactionDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getDateTimeDao(): DateTimeDao
    abstract fun getSavingDao(): SavingDao
    abstract fun getSavingHistoryDao():SavingHistoryDao

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
                    ).build()
                }
            return instance!!
        }
    }
}