package com.moony.calc.fragments

import android.content.Intent
import com.moony.calc.R
import com.moony.calc.activities.AddSavingGoalActivity
import com.moony.calc.activities.AddTransactionActivity
import com.moony.calc.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_transaction.*

class TransactionFragment : BaseFragment() {
    override fun init() {
        btn_add_finance.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    AddTransactionActivity::class.java
                )
            )
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_transaction
}
