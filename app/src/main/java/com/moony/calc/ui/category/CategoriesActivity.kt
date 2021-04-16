package com.moony.calc.ui.category

import android.os.Bundle
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.ActivityCategoriesBinding

class CategoriesActivity : BaseActivity() {
    private val binding: ActivityCategoriesBinding
        get() = (getViewBinding() as ActivityCategoriesBinding)

    companion object {
        const val KEY = "com.moony.calc.ui.category.CategoriesActivity"
        var isCanRemove = false
    }

    override fun initControls(savedInstanceState: Bundle?) {
        isCanRemove = intent.getBooleanExtra(KEY, false)

        val fragments = mutableListOf<BaseFragment>()
        val incomeFragment = CategoriesFragment(true, this)
        val expensesFragment = CategoriesFragment(false, this)
        fragments.add(expensesFragment)
        fragments.add(incomeFragment)


        val fragmentAdapter = CategoriesPagerAdapter(supportFragmentManager, fragments, this)
        binding.viewpagerCategories.adapter = fragmentAdapter
        binding.tabLayoutCategories.setupWithViewPager(binding.viewpagerCategories)

        binding.toolbarCategories.setNavigationOnClickListener {
            finish()
        }

    }

    override fun getLayoutId(): Int = R.layout.activity_categories
    override fun initEvents() {

    }

}
