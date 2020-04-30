package com.moony.calc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "date_table")
class DateTime(var day: Int, var month: String) :Serializable {
    @PrimaryKey(autoGenerate = true)
    var id=0
}