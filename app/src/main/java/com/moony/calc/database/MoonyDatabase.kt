package com.moony.calc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moony.calc.model.Category
import com.moony.calc.model.DateTime
import com.moony.calc.model.Saving
import com.moony.calc.model.Transaction

@Database(
    entities = [Transaction::class, Saving::class, Category::class, DateTime::class],
    version = 1
)
abstract class MoonyDatabase : RoomDatabase() {
    abstract fun getApplicationDao(): ApplicationDao

    companion object {
        @Volatile
        private var instance: MoonyDatabase? = null
        fun getInstance(context: Context): MoonyDatabase? {
            if (instance == null)
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MoonyDatabase::class.java,
                        "MoonyDatabase.db"
                    ).build()
                }
            return instance
        }
    }
}