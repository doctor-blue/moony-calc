package com.moony.calc.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

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
    var idSaving: Int,
    var amount: Double,
    var isSaving: Boolean,
    var date: String,
    var idTransaction: Int
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var idSavingHistory: Int = 0
}