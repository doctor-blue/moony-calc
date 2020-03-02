package com.moony.calc.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.fragments.TransactionFragment
import com.moony.calc.views.NavigationItemClick
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_menu.*


class MainActivity : BaseActivity(), NavigationItemClick {

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
                Toast.makeText(this, "card budget click", Toast.LENGTH_SHORT).show()
            }
            R.id.card_saving -> {
                Toast.makeText(this, "card saving click", Toast.LENGTH_SHORT).show()
            }
            R.id.card_chart -> {
                Toast.makeText(this, "card chart click", Toast.LENGTH_SHORT).show()
            }
            R.id.card_categories -> {
                Toast.makeText(this, "card categories click", Toast.LENGTH_SHORT).show()
            }
        }
    }


}

