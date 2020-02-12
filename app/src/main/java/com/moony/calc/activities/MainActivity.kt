package com.moony.calc.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.drawerlayout.widget.DrawerLayout
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity() {

    private var drawerToggle: ActionBarDrawerToggle? = null

    override fun init(savedInstanceState: Bundle?) {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle!!)
        card_saving_box.setOnClickListener {
            img_wallet.setImageResource(R.drawable.ic_wallet_no_color)
            img_saving_box.setImageResource(R.drawable.ic_piggy_color)
        }
        card_wallet.setOnClickListener {
            img_saving_box.setImageResource(R.drawable.ic_piggy_no_color)
            img_wallet.setImageResource(R.drawable.ic_wallet_with_color)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle!!.onConfigurationChanged(newConfig)
    }

}
