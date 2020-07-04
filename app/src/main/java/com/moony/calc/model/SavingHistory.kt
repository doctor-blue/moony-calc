package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saving_history")
class SavingHistory(
    var description: String,
    var idSaving: Int,
    var amount: Double,
    var isSaving: Boolean,
    var idCategory: Int,
    var date:String
) {
    @PrimaryKey(autoGenerate = true)
    var idSavingHistory: Int = 0
}