package com.moony.calc.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.Nullable


abstract class BaseActivity : AppCompatActivity() {
    abstract val layoutId: Int
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        init(savedInstanceState)

    }

    abstract fun init(savedInstanceState: Bundle?)
}
