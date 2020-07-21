package com.moony.calc.activities

import android.os.Bundle
import com.doctorblue.noname_library.OnItemSelectedListener
import com.moony.calc.R
import com.moony.calc.animation.MenuAnimation
import com.moony.calc.base.BaseActivity
import com.moony.calc.fragments.ChartFragment
import com.moony.calc.fragments.SavingBoxFragment
import com.moony.calc.fragments.TransactionFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), OnItemSelectedListener {
    private val savingBoxFragment = SavingBoxFragment()
    private val transactionFragment = TransactionFragment()
    private val chartFragment = ChartFragment()
    private var isLeftMenuShow = false

    override fun init(savedInstanceState: Bundle?) {
        initControl()
        initEvent()
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun initEvent() {

    }

    private fun initControl() {
        fragment_main.fragmentManager = supportFragmentManager

        no_name_bottom_bar.onItemSelectedListener = this
        no_name_bottom_bar.itemActiveIndex = 0
        no_name_bottom_bar.onItemReselected=onItemReselected
    }

    override fun onItemSelect(id: Int) {
        when (id) {
            R.id.transaction_fragment -> {
                fragment_main.replaceFragment(transactionFragment)
            }
            R.id.saving_fragment -> {
                fragment_main.replaceFragment(savingBoxFragment)
            }
            R.id.chart_fragment -> {
                fragment_main.replaceFragment(chartFragment)
            }
            R.id.more_fragment -> {
                /* startActivity(
                     Intent(this, CategoriesActivity::class.java).putExtra(
                         CategoriesActivity.KEY,
                         true
                     )
                 )*/

            }
        }
    }
    private val onItemReselected:(Int)->Unit={
        if (it==R.id.more_fragment ){
            isLeftMenuShow = if (!isLeftMenuShow){
                MenuAnimation.showLeftMenu(fragment_main, left_menu)
                true
            }else{
                MenuAnimation.hideLeftMenu(fragment_main, left_menu)
                false
            }
        }
    }

}

