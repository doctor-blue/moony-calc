package com.moony.calc.ui.category

import android.os.Bundle
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.base.BaseFragment
import kotlinx.android.synthetic.main.activity_categories.*

class CategoriesActivity : BaseActivity() {
    companion object{
        const val KEY="com.moony.calc.ui.category.CategoriesActivity"
        var isJustWatch=false
    }
    override fun init(savedInstanceState: Bundle?) {
        isJustWatch =intent.getBooleanExtra(KEY,false)

        val fragments = mutableListOf<BaseFragment>()
        val incomeFragment = CategoriesFragment(true, this)
        val expensesFragment = CategoriesFragment(false, this)
        fragments.add(expensesFragment)
        fragments.add(incomeFragment)


        val fragmentAdapter = CategoriesPagerAdapter(supportFragmentManager, fragments, this)
        viewpager_categories.adapter = fragmentAdapter
        tab_layout_categories.setupWithViewPager(viewpager_categories)

        toolbar_categories.setNavigationOnClickListener {
            finish()
        }

    }

    override fun getLayoutId(): Int = R.layout.activity_categories

}
