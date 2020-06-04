package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saving_goal")
class Saving(
    var description: String,
    var desiredAmount: Double,
    var deadLine: String,
    var idCategory: Int,
    var linkImage: String = "Empty"
) {
    @PrimaryKey(autoGenerate = true)
    var idSaving: Int = 0

}