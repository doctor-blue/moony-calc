package com.moony.calc.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
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

    override fun init(savedInstanceState: Bundle?) {
        initControl()
        initEvent()
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun initEvent() {
        //(this as AppCompatActivity).setSupportActionBar(app_bar_main)
        btn_nav_main.setOnClickListener(
            navigationIconClickListener
        )

    }

    @SuppressLint("ResourceAsColor")
    private fun initControl() {
        fragment_main.fragmentManager = supportFragmentManager
        fragment_main.replaceFragment(TransactionFragment())
        navigationIconClickListener = NavigationIconClickListener(
            this,
            fragment_main,
            AccelerateDecelerateInterpolator(),
            ContextCompat.getDrawable(this, R.drawable.ic_navigation), // Menu open icon
            ContextCompat.getDrawable(this, R.drawable.ic_menu_close)
        )
    }

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
                startActivity(Intent(this, CategoriesActivity::class.java))
                navigationIconClickListener.showBackdrop(btn_nav_main)
            }
        }
    }
}

