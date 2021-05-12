package com.moony.calc.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moony.calc.utils.SyncFlag
import com.moony.calc.utils.TimestampConverter
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "saving_history_table",
    foreignKeys = [ForeignKey(
        entity = Transaction::class,
        parentColumns = arrayOf("idTransaction"),
        childColumns = arrayOf("idTransaction"),
        onDelete = ForeignKey.CASCADE
    )]
)
class SavingHistory(
    var description: String,
    var idSaving: String,
    var amount: Double,
    var isSaving: Boolean,
    var date: String,
    var idTransaction: String,
    var syncFlag: String = SyncFlag.NONE.toString()
) : Serializable {
    @PrimaryKey
    var idSavingHistory: String = UUID.randomUUID().toString()
    @TypeConverters(TimestampConverter::class)
    var createDate: Date = Calendar.getInstance().time
}