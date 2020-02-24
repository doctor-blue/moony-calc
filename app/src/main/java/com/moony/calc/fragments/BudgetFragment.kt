package com.moony.calc.fragments

import android.content.Intent
import com.moony.calc.R
import com.moony.calc.activities.AddFinanceActivity
import com.moony.calc.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_budget.*

class BudgetFragment : BaseFragment() {
    override fun init() {
        btn_add_finance.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    AddFinanceActivity::class.java
                )
            )
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_budget
}
