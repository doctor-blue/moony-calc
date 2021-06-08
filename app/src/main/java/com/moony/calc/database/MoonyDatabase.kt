package com.moony.calc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moony.calc.R
import com.moony.calc.model.*
import com.moony.calc.utils.TimestampConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

@Database(
    entities = [Transaction::class, Saving::class, Category::class, SavingHistory::class],
    version = 1
)
@TypeConverters(TimestampConverter::class)
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
                adDefaultCategory(instance!!.getCategoryDao())
            }
        }

        private fun adDefaultCategory(categoryDao: CategoryDao) {
            GlobalScope.launch(Dispatchers.IO) {
                categoryDao.insertCategory(Category("","categories/income/salary.png",resId = R.string.saving))
                categoryDao.insertCategory(Category("","categories/education/book.png",resId = R.string.books))
                categoryDao.insertCategory(Category("","categories/electronics/phone.png",resId = R.string.phones))
                categoryDao.insertCategory(Category("","categories/entertainment/gamepad.png",resId = R.string.games))
                categoryDao.insertCategory(Category("","categories/entertainment/cinema.png",resId = R.string.cinema))
                categoryDao.insertCategory(Category("","categories/family/baby.png",resId = R.string.baby))
                categoryDao.insertCategory(Category("","categories/family/cat.png",resId = R.string.pet))
                categoryDao.insertCategory(Category("","categories/fitness/workout.png",resId = R.string.fitnesss))
                categoryDao.insertCategory(Category("","categories/food/burger.png",resId = R.string.foods))
                categoryDao.insertCategory(Category("","categories/food/juice.png",resId = R.string.drinks))
                categoryDao.insertCategory(Category("","categories/food/french_fries.png",resId = R.string.snacks))
                categoryDao.insertCategory(Category("","categories/furniture/wrench.png",resId = R.string.repair))
                categoryDao.insertCategory(Category("","categories/life/travel.png",resId = R.string.travel))
                categoryDao.insertCategory(Category("","categories/life/heart.png",resId = R.string.lover))
                categoryDao.insertCategory(Category("","categories/medical/medicine.png",resId = R.string.medicine))
                categoryDao.insertCategory(Category("","categories/medical/vacine.png",resId = R.string.health_check))
                categoryDao.insertCategory(Category("","categories/shopping/shoppingonline.png",resId = R.string.shoppingg))
                categoryDao.insertCategory(Category("","categories/transportation/station.png",resId = R.string.bus_ticket))
                categoryDao.insertCategory(Category("","categories/transportation/train.png",resId = R.string.train_ticket))
                categoryDao.insertCategory(Category("","categories/transportation/plane.png",resId = R.string.plane_ticket))
                categoryDao.insertCategory(Category("","categories/transportation/taxi.png",resId = R.string.taxi))
                categoryDao.insertCategory(Category("","categories/transportation/parking_lot.png",resId = R.string.parking))
                categoryDao.insertCategory(Category("","categories/transportation/fuel.png",resId = R.string.refuel))
                categoryDao.insertCategory(Category("","categories/money/pay.png",resId = R.string.loan))
                categoryDao.insertCategory(Category("","categories/money/money.png",resId = R.string.other))


                categoryDao.insertCategory(Category("","categories/income/salary.png",true,resId = R.string.saving))

            }
        }

    }

}