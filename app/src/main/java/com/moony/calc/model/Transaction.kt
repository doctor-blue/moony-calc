package com.moony.calc.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moony.calc.utils.DateConverter
import com.moony.calc.utils.SyncFlag
import com.moony.calc.utils.TimestampConverter
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "transaction_table",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("idCategory"),
        childColumns = arrayOf("idCategory"),
        onDelete = ForeignKey.CASCADE
    )]
)
class Transaction(
    var money: Double,
    var idCategory: String,
    var note: String,
    var day: Int,
    var month: Int,
    var year: Int,
    var syncFlag: String = SyncFlag.NONE.toString(),
    @TypeConverters(TimestampConverter::class)
    var transactionTime: Date? = null
) : Serializable {
    @PrimaryKey
    var idTransaction: String = UUID.randomUUID().toString()

    @TypeConverters(TimestampConverter::class)
    var createDate: Date = Calendar.getInstance().time

}