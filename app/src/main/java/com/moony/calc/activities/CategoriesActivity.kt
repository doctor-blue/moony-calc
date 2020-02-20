package com.moony.calc.activities

import android.os.Bundle
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.fragments.BudgetChartFragment
import com.moony.calc.fragments.SavingChartFragment
import kotlinx.android.synthetic.main.activity_chart.*

class CategoriesActivity : BaseActivity(){
    override fun init(savedInstanceState: Bundle?) {

    }

    override fun getLayoutId(): Int = R.layout.activity_chart

}
