package com.moony.calc.ui.saving.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.moony.calc.R
import com.moony.calc.base.BaseFragment

class SavingDetailAdapter(fm: FragmentManager, val fragments: List<Fragment>,val context: Context) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = fragments[position]
    override fun getCount(): Int = fragments.size
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0-> context.resources.getText(R.string.goal)
            else->context.resources.getText(R.string.history_saving)
        }
    }
}