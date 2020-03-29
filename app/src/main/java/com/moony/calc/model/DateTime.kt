package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date_table")
class DateTime {
    @PrimaryKey(autoGenerate = true)
    var id=0
    var day:Int=0
    var month:String=""

    constructor(day:Int,month:String){
        this.day=day
        this.month=month
    }
    constructor()


}