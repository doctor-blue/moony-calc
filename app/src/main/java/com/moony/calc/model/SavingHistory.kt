package com.moony.calc.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "saving_history_table",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("idCategory"),
        childColumns = arrayOf("idCategory"),
        onDelete = ForeignKey.CASCADE
    )])
class SavingHistory(
    var description: String,
    var idSaving: Int,
    var amount: Double,
    var isSaving: Boolean,
    var idCategory: Int,
    var date:String
):Serializable {
    @PrimaryKey(autoGenerate = true)
    var idSavingHistory: Int = 0
}