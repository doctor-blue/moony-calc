package com.moony.calc.activities

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.fragments.ChartFragment
import com.moony.calc.fragments.SavingBoxFragment
import com.moony.calc.fragments.TransactionFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    private val savingBoxFragment = SavingBoxFragment()
    private val transactionFragment = TransactionFragment()
    private val chartFragment = ChartFragment()

    private lateinit var navController: NavController

    override fun init(savedInstanceState: Bundle?) {
        initControl()
        initEvent()
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun initEvent() {

    }

    private fun initControl() {
        navController = findNavController(R.id.main_fragment)
        no_name_bottom_bar.setupWithNavController(navController)
    }

    override fun navigateUpTo(upIntent: Intent?): Boolean {
        navController.navigateUp()
        return true
    }


}

