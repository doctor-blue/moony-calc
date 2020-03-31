package com.moony.calc.activities

import android.content.Intent
import android.os.Bundle
import com.moony.calc.R
import com.moony.calc.base.BaseActivity


class SplashScreenActivity : BaseActivity() {
    override fun getLayoutId(): Int =R.layout.activity_splash_screen

    override fun init(savedInstanceState: Bundle?) {
        val intent = Intent(this, MainActivity::class.java)
        val thread: Thread = object : Thread() {
            override fun run() {
                super.run()
                try {
                    sleep(4000)
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
