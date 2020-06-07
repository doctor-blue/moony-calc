package com.moony.calc.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.moony.calc.R
import com.moony.calc.base.BaseFragment

class CategoriesPagerAdapter(
    fm: FragmentManager,
    val fragments: List<BaseFragment>,
    val context: Context
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> fragments[1]
            else -> fragments[0]
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.resources.getString(R.string.expenses)
            else -> context.resources.getString(R.string.income)

        }
    }

}