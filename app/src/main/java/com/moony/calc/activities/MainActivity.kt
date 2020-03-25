package com.moony.calc.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.fragments.CategoriesFragment
import com.moony.calc.fragments.ChartFragment
import com.moony.calc.fragments.SavingBoxFragment
import com.moony.calc.fragments.TransactionFragment
import com.moony.calc.views.NavigationItemClick
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_menu.*


class MainActivity : BaseActivity(), NavigationItemClick {
    private val savingBoxFragment=SavingBoxFragment()
    private val transactionFragment=TransactionFragment()
    private val chartFragment=ChartFragment()

    override fun init(savedInstanceState: Bundle?) {
        initControl()
        initEvent()
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun initEvent() {

    }

    @SuppressLint("ResourceAsColor")
    private fun initControl() {
        navigation_menu.itemClick = this
        fragment_main.replaceFragment(TransactionFragment(), supportFragmentManager)
    }

    override fun onClick(view: View) {
        main_root.transitionToStart()
        when (view.id) {
            R.id.card_budget -> {
                fragment_main.replaceFragment(transactionFragment, supportFragmentManager)
            }
            R.id.card_saving -> {
                fragment_main.replaceFragment(savingBoxFragment, supportFragmentManager)
            }
            R.id.card_chart -> {
                fragment_main.replaceFragment(chartFragment, supportFragmentManager)
            }
            R.id.card_categories -> {
                startActivity(Intent(this,CategoriesActivity::class.java))

            }
        }
    }


}

