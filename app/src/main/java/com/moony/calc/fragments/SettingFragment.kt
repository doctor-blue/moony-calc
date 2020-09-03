package com.moony.calc.fragments

import android.widget.ArrayAdapter
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseFragment() {
    override fun init() {
        initEvent()
        initControl()
    }

    private fun initEvent() {
    }

    private fun initControl() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Currency,
            R.layout.spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            settings_spinner.adapter = arrayAdapter

        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_setting

}