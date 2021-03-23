package com.moony.calc.model

import androidx.room.Embedded
import androidx.room.Relation

class SavingItem(
    @Embedded
    var saving: Saving,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idCategory"
    )
    var category: Category
) {
}