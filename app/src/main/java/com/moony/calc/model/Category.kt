package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
class Category(var title: String, var iconUrl: String,var isIncome:Boolean=false) {
    @PrimaryKey(autoGenerate = true)
    var idCategory:Int=0
}