package com.moony.calc.model

import androidx.room.Embedded

class SavingItem(
    @Embedded
    var saving: Saving,
    var sum: Double
) {
    override fun toString(): String {
        return saving.title
    }
}