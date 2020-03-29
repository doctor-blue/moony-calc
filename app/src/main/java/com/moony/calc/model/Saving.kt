package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saving_goal")
class Saving(
    var description: String,
    var currentAmount: Double,
    var desiredAmount: Double,
    var idCategory: Int
) {
    @PrimaryKey(autoGenerate = true)
    var idSaving: Int = 0

}