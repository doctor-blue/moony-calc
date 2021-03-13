package com.moony.calc.model

import androidx.room.Embedded
import androidx.room.Relation

class TransactionItem (
    @Embedded
    var transaction: Transaction,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idCategory"
    )
    var category: Category
)