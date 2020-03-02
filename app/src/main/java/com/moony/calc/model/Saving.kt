package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saving_goal")
class Saving() {
    @PrimaryKey(autoGenerate = true)
    var idSaving: Int = 0
    var description: String = "Empty"
    var currentAmount: Double = 0.0
    var desiredAmount: Double = 0.0
    var idCategory: Int = 0

    constructor(description: String, currentAmount: Double, desiredAmount: Double, idCategory: Int) : this() {
        this.description = description
        this.currentAmount = currentAmount
        this.desiredAmount = desiredAmount
        this.idCategory = idCategory
    }
}