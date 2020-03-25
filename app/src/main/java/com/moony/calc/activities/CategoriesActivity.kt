package com.moony.calc.activities

import android.os.Bundle
import com.moony.calc.R
import com.moony.calc.adapter.CategoriesPagerAdapter
import com.moony.calc.base.BaseActivity
import kotlinx.android.synthetic.main.activity_categories.*

class CategoriesActivity : BaseActivity() {
    override fun init(savedInstanceState: Bundle?) {
        val fragmentAdapter = CategoriesPagerAdapter(supportFragmentManager)
        viewpager_categories.adapter = fragmentAdapter

        tab_layout_categories.setupWithViewPager(viewpager_categories)
    }

    override fun getLayoutId(): Int = R.layout.activity_categories

}
