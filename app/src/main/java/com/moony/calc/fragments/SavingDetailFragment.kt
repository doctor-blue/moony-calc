package com.moony.calc.fragments

import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_saving_detail.*

class SavingDetailFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_saving_detail
    override fun init() {
        wv_saving_detail.setProgress(55)
    }
}
