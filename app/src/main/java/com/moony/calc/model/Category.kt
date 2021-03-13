package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "category_table")
class Category(var title: String, var iconUrl: String,var isIncome:Boolean=false) :Serializable{
    @PrimaryKey(autoGenerate = true)
    var idCategory:Int=0
}