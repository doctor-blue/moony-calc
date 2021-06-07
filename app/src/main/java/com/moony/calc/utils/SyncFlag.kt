package com.moony.calc.utils

enum class SyncFlag(private val des:String) {
    DELETE("DELETE"),
    UPDATE("UPDATE"),
    INSERT("INSERT"),
    NONE("NONE");

    override fun toString(): String {
        return des
    }

}