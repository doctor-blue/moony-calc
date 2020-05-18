package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "date_table")
class DateTime(var day: Int, var month: Int,var year:Int) :Serializable {
    @PrimaryKey(autoGenerate = true)
    var id=0
}