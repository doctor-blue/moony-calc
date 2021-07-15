package com.moony.calc.activities

import android.content.Intent
import android.os.Bundle
import com.devcomentry.moonlight.binding.BindingActivity
import com.moony.calc.R
import com.moony.calc.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : BindingActivity<ActivitySplashScreenBinding>(R.layout.activity_splash_screen) {

    override fun initControls(savedInstanceState: Bundle?) {
        val intent = Intent(this, MainActivity::class.java)
        val thread: Thread = object : Thread() {
            override fun run() {
                super.run()
                try {
                    sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    startActivity(intent)
                    finish()
                }
            }
        }
        thread.start()

    }


}
