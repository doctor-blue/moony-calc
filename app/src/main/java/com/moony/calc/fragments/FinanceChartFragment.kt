package com.moony.calc.fragments

import android.view.View
import com.google.android.material.appbar.MaterialToolbar
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_finance_chart.*

class FinanceChartFragment : BaseFragment() {
    override fun init(view:View) {
        val toolbar=view.findViewById<MaterialToolbar>(R.id.toolbar_finance_chart)
        toolbar.setNavigationOnClickListener { activity!!.finish() }
    }

    override fun getLayoutId(): Int = R.layout.fragment_finance_chart
}