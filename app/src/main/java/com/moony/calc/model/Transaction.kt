package com.moony.calc.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transaction_table",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("idCategory"),
        childColumns = arrayOf("idCategory"),
        onDelete = ForeignKey.CASCADE
    )])
class Transaction(
    var money: Double,
    var isIncome: Boolean,
    var idCategory: Int,
    var note: String,
    var day: Int,
    var month: Int,
    var year: Int
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var idTransaction: Int = 0

}