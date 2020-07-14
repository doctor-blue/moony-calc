package com.moony.calc.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.fragments.ChartFragment
import com.moony.calc.fragments.SavingBoxFragment
import com.moony.calc.fragments.TransactionFragment
import com.moony.calc.views.NavigationIconClickListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    private val savingBoxFragment = SavingBoxFragment()
    private val transactionFragment = TransactionFragment()
    private val chartFragment = ChartFragment()

    private lateinit var navigationIconClickListener: NavigationIconClickListener
    private lateinit var navController: NavController

    override fun init(savedInstanceState: Bundle?) {
        initControl()
        initEvent()
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun initEvent() {
        /* btn_nav_main.setOnClickListener(
             navigationIconClickListener
         )*/

    }

    private fun initControl() {
        /* fragment_main.fragmentManager = supportFragmentManager
         fragment_main.replaceFragment(TransactionFragment())
         layout_main_menu.visibility = View.GONE

         navigationIconClickListener = NavigationIconClickListener(
             this,
             fragment_main,
             AccelerateDecelerateInterpolator(),
             ContextCompat.getDrawable(this, R.drawable.ic_navigation), // Menu open icon
             ContextCompat.getDrawable(this, R.drawable.ic_menu_close),
             layout_main_menu
         )*/
        navController = findNavController(R.id.main_fragment)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_menu, menu)
        bottomBar.setupWithNavController(menu!!, navController)
        return true
    }
   

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
    }

/*
    fun onMenuItemClick(view: View) {
        when (view.id) {
            R.id.mnu_budget -> {
                fragment_main.replaceFragment(transactionFragment)
                navigationIconClickListener.showBackdrop(btn_nav_main)
            }
            R.id.mnu_saving -> {
                fragment_main.replaceFragment(savingBoxFragment)
                navigationIconClickListener.showBackdrop(btn_nav_main)
            }
            R.id.mnu_chart -> {
                fragment_main.replaceFragment(chartFragment)
                navigationIconClickListener.showBackdrop(btn_nav_main)
            }
            R.id.mnu_categories -> {
                startActivity(
                    Intent(this, CategoriesActivity::class.java).putExtra(
                        CategoriesActivity.KEY,
                        true
                    )
                )
                navigationIconClickListener.showBackdrop(btn_nav_main)
            }
        }
    }*/
}

