package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
class Category() {
    @PrimaryKey(autoGenerate = true)
    var idCategory:Int=0
    var title:String="Empty"
    var isIncome:Boolean=false
    var iconUrl:String=""

    constructor(title: String, iconUrl: String):this() {
        this.title = title
        this.iconUrl = iconUrl
    }
}