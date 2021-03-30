package com.moony.calc.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

class ChartItem (
    @Embedded
    var category: Category,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idCategory"
    )
    var transaction: Transaction,
    var sum: Double
): Serializable