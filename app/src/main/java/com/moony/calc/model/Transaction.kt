package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transaction_table")
class Transaction (
    var money: Double,
    var isIncome: Boolean,
    var idDate: Int,
    var idCategory: Int,
    var note:String,
    var month:Int,
    var year:Int
) :Serializable{
    @PrimaryKey(autoGenerate = true)
    var idTransaction:Int=0

}