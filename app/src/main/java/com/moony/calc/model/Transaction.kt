package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transaction_table")
class Transaction (
    var money: Double,
    var isIncome: Boolean,
    var month: String,
    var idDate: Int,
    var idCategory: Int,
    var note:String
) :Serializable{
    @PrimaryKey(autoGenerate = true)
    var idTransaction:Int=0

}