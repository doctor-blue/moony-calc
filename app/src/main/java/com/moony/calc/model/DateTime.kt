package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date_table")
class DateTime(var day: Int, var month: String) {
    @PrimaryKey(autoGenerate = true)
    var id=0
}