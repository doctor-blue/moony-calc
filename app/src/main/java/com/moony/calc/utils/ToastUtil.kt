package com.moony.calc.utils

import android.content.Context
import android.widget.Toast

fun Context.showToast(
    messId:Int
){
    Toast.makeText(this,messId,Toast.LENGTH_SHORT).show()
}