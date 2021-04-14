package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "saving_goal")
class Saving(
    var title: String,
    var desiredAmount: Double,
    var deadLine: String,
    var imageLink: String = "Empty",
    var iconUrl: String = "",
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var idSaving: Int = 0

    override fun toString(): String {
        return title
    }

}