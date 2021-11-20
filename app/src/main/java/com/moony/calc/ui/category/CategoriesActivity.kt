package com.moony.calc.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.devcomentry.moonlight.binding.BindingActivity
import com.moony.calc.R
import com.moony.calc.databinding.ActivityCategoriesBinding
import com.moony.calc.ui.category.adapter.CategoriesPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesActivity :
    BindingActivity<ActivityCategoriesBinding>(R.layout.activity_categories) {

    companion object {
        const val KEY = "com.moony.calc.ui.category.CategoriesActivity"
        var isCanRemove = false
    }

    override fun initControls(savedInstanceState: Bundle?) {
        isCanRemove = intent.getBooleanExtra(KEY, false)

        val fragments = mutableListOf<Fragment>()
        val incomeFragment = CategoriesFragment(true, this)
        val expensesFragment = CategoriesFragment(false, this)
        fragments.add(expensesFragment)
        fragments.add(incomeFragment)


       binding {
           val fragmentAdapter = CategoriesPagerAdapter(supportFragmentManager, fragments, this@CategoriesActivity)
           viewpagerCategories.adapter = fragmentAdapter
           tabLayoutCategories.setupWithViewPager(viewpagerCategories)

           toolbarCategories.setNavigationOnClickListener {
               finish()
           }
       }

    }

}
