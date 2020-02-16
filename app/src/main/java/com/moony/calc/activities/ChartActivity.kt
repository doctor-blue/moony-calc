package com.moony.calc.activities

import android.os.Bundle
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.fragments.FinanceChartFragment
import com.moony.calc.fragments.SavingChartFragment
import kotlinx.android.synthetic.main.activity_chart.*

class ChartActivity : BaseActivity(){
    override fun init(savedInstanceState: Bundle?) {
        if (intent.getBooleanExtra("isFinanceChart",true))
            chart_fragment.replaceFragment(FinanceChartFragment(),supportFragmentManager)
        else
            chart_fragment.replaceFragment(SavingChartFragment(),supportFragmentManager)

    }

    override fun getLayoutId(): Int = R.layout.activity_chart

}
