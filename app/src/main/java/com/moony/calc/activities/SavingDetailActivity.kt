package com.moony.calc.activities

import android.os.Bundle
import android.view.Menu
import com.moony.calc.R
import com.moony.calc.adapter.SavingDetailAdapter
import com.moony.calc.base.BaseActivity
import com.moony.calc.base.BaseFragment
import com.moony.calc.fragments.SavingDetailFragment
import com.moony.calc.fragments.SavingHistoryFragment
import kotlinx.android.synthetic.main.activity_saving_detail.*


class SavingDetailActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_saving_detail

    override fun init(savedInstanceState: Bundle?) {
        val fragments: List<BaseFragment> =
            mutableListOf(SavingDetailFragment(), SavingHistoryFragment())
        val savingDetailAdapter = SavingDetailAdapter(supportFragmentManager, fragments,this)
        viewpager_detail_saving.adapter = savingDetailAdapter
        tab_layout_saving.setupWithViewPager(viewpager_detail_saving)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.saving_detail_menu, menu)
        return true
    }
}