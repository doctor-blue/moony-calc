package com.moony.calc.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {
    private val binding: ActivityMainBinding
        get() = (getViewBinding() as ActivityMainBinding)

    private lateinit var navController: NavController

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initEvents() {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.transaction_fragment || destination.id == R.id.saving_fragment || destination.id == R.id.chart_fragment || destination.id == R.id.setting_fragment) {
                binding.noNameBottomBar.show()
                Log.d("MainActivity", "show + ${binding.noNameBottomBar.isShow}")
            } else {
                binding.noNameBottomBar.hide()
                Log.d("MainActivity", "hide + ${binding.noNameBottomBar.isShow}")
            }
        }

    }

    override fun initControls(savedInstanceState: Bundle?) {
        navController = findNavController(R.id.main_fragment)
        binding.noNameBottomBar.setupWithNavController(navController)
    }

    override fun navigateUpTo(upIntent: Intent?): Boolean {
        navController.navigateUp()
        return true
    }


}

