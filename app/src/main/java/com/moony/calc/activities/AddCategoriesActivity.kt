package com.moony.calc.activities

import android.os.Bundle
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_categories.*


class AddCategoriesActivity : BaseActivity() {
    private var isIncome = true

    companion object {
        val KEY = "com.moony.calc.activities.AddCategoriesActivity"
    }

    override fun init(savedInstanceState: Bundle?) {
        val intent = intent

        intent.let {
            isIncome = intent.getBooleanExtra(KEY, true)
        }

        if (isIncome) {
            toolbar_add_categories.title = resources.getString(R.string.add_income_category)
        } else {
            toolbar_add_categories.title = resources.getString(R.string.add_expense_category)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_add_categories
}
