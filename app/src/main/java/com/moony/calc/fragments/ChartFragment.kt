package com.moony.calc.fragments

import com.moony.calc.R
import com.moony.calc.adapter.ChartPageAdapter
import com.moony.calc.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_chart.*

class ChartFragment : BaseFragment() {
    override fun init() {
        val fragments= mutableListOf<BaseFragment>()
        val transactionChartFragment=TransactionChartFragment()
        val savingChartFragment=SavingChartFragment()
        fragments.add(transactionChartFragment)
        fragments.add(savingChartFragment)
        val chartPagerAdapter=ChartPageAdapter(activity!!.supportFragmentManager,fragments,baseContext!!)
        view_pager_chart.adapter=chartPagerAdapter
        tab_layout_chart.setupWithViewPager(view_pager_chart)
    }

    override fun getLayoutId(): Int = R.layout.fragment_chart
}