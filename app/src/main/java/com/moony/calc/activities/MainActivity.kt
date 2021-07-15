package com.moony.calc.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.devcomentry.moonlight.binding.BindingActivity
import com.moony.calc.R
import com.moony.calc.databinding.ActivityMainBinding


class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun initEvents() {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.transaction_fragment || destination.id == R.id.saving_fragment || destination.id == R.id.chart_fragment || destination.id == R.id.setting_fragment) {
                binding.bottomBar.show()
                Log.d("MainActivity", "show + ${binding.bottomBar.isShow}")
            } else {
                binding.bottomBar.hide()
                Log.d("MainActivity", "hide + ${binding.bottomBar.isShow}")
            }
        }

    }

    override fun initControls(savedInstanceState: Bundle?) {
        navController = findNavController(R.id.main_fragment)
        binding.bottomBar.setupWithNavController(navController)
    }

    override fun navigateUpTo(upIntent: Intent?): Boolean {
        navController.navigateUp()
        return true
    }


}

