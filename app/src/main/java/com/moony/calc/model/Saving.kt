package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moony.calc.utils.SyncFlag
import com.moony.calc.utils.TimestampConverter
import java.io.Serializable
import java.util.*

@Entity(tableName = "saving_goal_table")
class Saving(
    var title: String,
    var desiredAmount: Double,
    @TypeConverters(TimestampConverter::class)
    var deadLine: Date,
    var imageLink: String = "Empty",
    var iconUrl: String = "",
    var syncFlag: String = SyncFlag.NONE.toString()
) : Serializable {
    @PrimaryKey
    var idSaving: String = UUID.randomUUID().toString()

    @TypeConverters(TimestampConverter::class)
    var createDate: Date = Calendar.getInstance().time

    override fun toString(): String {
        return title
    }

}