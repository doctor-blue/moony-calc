package com.moony.calc.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    private lateinit var navController: NavController

    override fun init(savedInstanceState: Bundle?) {
        initControl()
        initEvent()
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun initEvent() {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.transaction_fragment || destination.id == R.id.saving_fragment || destination.id == R.id.chart_fragment) {
                no_name_bottom_bar.show()
                Log.d("MainActivity", "show + ${no_name_bottom_bar.isShow}")
            } else {
                no_name_bottom_bar.hide()
                Log.d("MainActivity", "hide + ${no_name_bottom_bar.isShow}")
            }
        }

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

