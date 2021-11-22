package com.moony.calc.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moony.calc.R
import com.moony.calc.model.Category
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingHistory
import com.moony.calc.model.Transaction
import com.moony.calc.utils.TimestampConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    lateinit var context: Context

    companion object {
        @SuppressLint("StaticFieldLeak")
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
                    instance!!.context = context
                }
            return instance!!
        }

        private val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                addDefaultCategory(instance!!.getCategoryDao(), instance!!.context)
            }
        }

        private fun addDefaultCategory(categoryDao: CategoryDao,context: Context) {
            GlobalScope.launch(Dispatchers.IO) {
                val defaultCategories = listOf(
                    Category(context.getString(R.string.atm), "categories/life/atm.png"),
                    Category(context.getString(R.string.books), "categories/education/book.png"),
                    Category(context.getString(R.string.phones), "categories/electronics/phone.png"),
                    Category(context.getString(R.string.games), "categories/entertainment/gamepad.png"),
                    Category(context.getString(R.string.cinema), "categories/entertainment/cinema.png"),
                    Category(context.getString(R.string.baby), "categories/family/baby.png"),
                    Category(context.getString(R.string.pet), "categories/family/cat.png"),
                    Category(context.getString(R.string.fitness), "categories/fitness/workout.png"),
                    Category(context.getString(R.string.foods), "categories/food/burger.png"),
                    Category(context.getString(R.string.drinks), "categories/food/juice.png"),
                    Category(context.getString(R.string.snacks), "categories/food/french_fries.png"),
                    Category(context.getString(R.string.repair), "categories/furniture/wrench.png"),
                    Category(context.getString(R.string.travel), "categories/life/travel.png"),
                    Category(context.getString(R.string.lover), "categories/life/heart.png"),
                    Category(context.getString(R.string.insurrance), "categories/life/baohiem.png"),
                    Category(context.getString(R.string.bill), "categories/life/bill.png"),
                    Category(context.getString(R.string.rent), "categories/life/rent.png"),
                    Category(context.getString(R.string.wedding), "categories/life/wedding.png"),
                    Category(context.getString(R.string.charity), "categories/life/charity.png"),
                    Category(context.getString(R.string.party), "categories/life/party.png"),
                    Category(context.getString(R.string.medicine), "categories/medical/medicine.png"),
                    Category(context.getString(R.string.health_check), "categories/medical/vacine.png"),
                    Category(
                        context.getString(R.string.shopping),
                        "categories/shopping/shoppingonline.png"
                    ),
                    Category(context.getString(R.string.clothes), "categories/shopping/clothes.png"),
                    Category(context.getString(R.string.footwear), "categories/shopping/footwear.png"),
                    Category(
                        context.getString(R.string.bus_ticket),
                        "categories/transportation/station.png",
                    ),
                    Category(
                        context.getString(R.string.train_ticket),
                        "categories/transportation/train.png",
                    ),
                    Category(
                        context.getString(R.string.plane_ticket),
                        "categories/transportation/plane.png",
                    ),
                    Category(context.getString(R.string.taxi), "categories/transportation/taxi.png"),
                    Category(
                        context.getString(R.string.parking),
                        "categories/transportation/parking_lot.png",
                    ),
                    Category(context.getString(R.string.refuel), "categories/transportation/fuel.png"),
                    Category(context.getString(R.string.saving), "categories/income/salary.png"),
                    Category(context.getString(R.string.other), "categories/money/money.png"),

                    Category(context.getString(R.string.sell), "categories/income/sell.png", true),
                    Category(
                        context.getString(R.string.bank_interest),
                        "categories/income/interest.png",
                        true,
                    ),
                    Category(context.getString(R.string.bonus), "categories/income/bonus.png", true),
                    Category(
                        context.getString(R.string.monthly_salary),
                        "categories/income/salary1.png",
                        true,
                    ),
                    Category(context.getString(R.string.saving), "categories/income/salary.png", true),
                    Category(context.getString(R.string.other), "categories/money/money.png", true)
                )
                defaultCategories.forEach {
                    categoryDao.insertCategory(it)
                }
            }
        }

    }

}