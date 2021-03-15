package com.moony.calc.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

class TransactionItem(
    @Embedded
    var transaction: Transaction,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idCategory"
    )
    var category: Category
) : Serializable