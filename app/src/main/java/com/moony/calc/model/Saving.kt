package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moony.calc.utils.SyncFlag
import java.io.Serializable
import java.util.*

@Entity(tableName = "saving_goal")
class Saving(
    var title: String,
    var desiredAmount: Double,
    var deadLine: String,
    var imageLink: String = "Empty",
    var iconUrl: String = "",
    var syncFlag: String = SyncFlag.NONE.toString()
) : Serializable {
    @PrimaryKey
    var idSaving: String = UUID.randomUUID().toString()

    override fun toString(): String {
        return title
    }

}