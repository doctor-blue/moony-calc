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
                val defaultCategories = listOf(
                    Category("","categories/life/atm.png",resId = R.string.atm),
                    Category("","categories/education/book.png",resId = R.string.books),
                    Category("","categories/electronics/phone.png",resId = R.string.phones),
                    Category("","categories/entertainment/gamepad.png",resId = R.string.games),
                    Category("","categories/entertainment/cinema.png",resId = R.string.cinema),
                    Category("","categories/family/baby.png",resId = R.string.baby),
                    Category("","categories/family/cat.png",resId = R.string.pet),
                    Category("","categories/fitness/workout.png",resId = R.string.fitnesss),
                    Category("","categories/food/burger.png",resId = R.string.foods),
                    Category("","categories/food/juice.png",resId = R.string.drinks),
                    Category("","categories/food/french_fries.png",resId = R.string.snacks),
                    Category("","categories/furniture/wrench.png",resId = R.string.repair),
                    Category("","categories/life/travel.png",resId = R.string.travel),
                    Category("","categories/life/heart.png",resId = R.string.lover),
                    Category("","categories/life/baohiem.png",resId = R.string.insurrance),
                    Category("","categories/life/bill.png",resId = R.string.bill),
                    Category("","categories/life/rent.png",resId = R.string.rent),
                    Category("","categories/life/wedding.png",resId = R.string.wedding),
                    Category("","categories/life/charity.png",resId = R.string.charity),
                    Category("","categories/life/party.png",resId = R.string.party),
                    Category("","categories/medical/medicine.png",resId = R.string.medicine),
                    Category("","categories/medical/vacine.png",resId = R.string.health_check),
                    Category("","categories/shopping/shoppingonline.png",resId = R.string.shoppingg),
                    Category("","categories/shopping/clothes.png",resId = R.string.clothes),
                    Category("","categories/shopping/footwear.png",resId = R.string.footwear),
                    Category("","categories/transportation/station.png",resId = R.string.bus_ticket),
                    Category("","categories/transportation/train.png",resId = R.string.train_ticket),
                    Category("","categories/transportation/plane.png",resId = R.string.plane_ticket),
                    Category("","categories/transportation/taxi.png",resId = R.string.taxi),
                    Category("","categories/transportation/parking_lot.png",resId = R.string.parking),
                    Category("","categories/transportation/fuel.png",resId = R.string.refuel),
                    Category("","categories/income/salary.png",resId = R.string.saving),
                    Category("","categories/money/money.png",resId = R.string.other),

                    Category("","categories/income/sell.png",true,resId = R.string.sell),
                    Category("","categories/income/interest.png",true,resId = R.string.bank_interest),
                    Category("","categories/income/bonus.png",true,resId = R.string.bonus),
                    Category("","categories/income/salary1.png",true,resId = R.string.salary),
                    Category("","categories/income/salary.png",true,resId = R.string.saving),
                    Category("","categories/money/money.png",true,resId = R.string.other)
                )
                defaultCategories.forEach{
                    categoryDao.insertCategory(it)
                }
            }
        }

    }

}