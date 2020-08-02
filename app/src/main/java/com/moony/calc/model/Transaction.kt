package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*

@Entity(tableName = "transaction_table")
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