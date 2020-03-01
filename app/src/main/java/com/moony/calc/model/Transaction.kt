package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Transaction")
class Transaction() {
    @PrimaryKey(autoGenerate = true)
    var idTransaction:Int=0
    var money:Double=0.0
    var isIncome:Boolean=false
    var month:String="Empty"
    var day:Int=0
    var idCategory:Int=0

    constructor(money: Double, isIncome: Boolean, month: String, day: Int, idCategory: Int):this() {
        this.money = money
        this.isIncome = isIncome
        this.month = month
        this.day = day
        this.idCategory = idCategory
    }
}