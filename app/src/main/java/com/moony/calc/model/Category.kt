package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moony.calc.utils.SyncFlag
import com.moony.calc.utils.TimestampConverter
import java.io.Serializable
import java.util.*

@Entity(tableName = "category_table")
class Category(
    var title: String,
    var iconUrl: String,
    var isIncome: Boolean = false,
    val resId: Int = -1,
    var syncFlag: String = SyncFlag.NONE.toString()
) : Serializable {
    @PrimaryKey
    var idCategory: String = UUID.randomUUID().toString()

    @TypeConverters(TimestampConverter::class)
    var createDate: Date = Calendar.getInstance().time
}