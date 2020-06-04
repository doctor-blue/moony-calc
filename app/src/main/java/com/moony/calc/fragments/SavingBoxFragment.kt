package com.moony.calc.fragments

import android.content.Intent
import com.moony.calc.R
import com.moony.calc.activities.AddSavingGoalActivity
import com.moony.calc.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_saving_box.*

class SavingBoxFragment : BaseFragment() {
    override fun init() {
        initControl()
        initEvent()
    }

    private fun initEvent() {
        btn_add_saving_goals.setOnClickListener {
            startActivity(Intent(activity,AddSavingGoalActivity::class.java))
        }
    }

    private fun initControl() {

    }

    override fun getLayoutId(): Int = R.layout.fragment_saving_box


}