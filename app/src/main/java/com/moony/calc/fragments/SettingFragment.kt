package com.moony.calc.fragments

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.utils.Settings
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseFragment() {

    private val settings: Settings by lazy {
        Settings.getInstance(requireContext())
    }
    private val currencyArr: Array<String> by lazy {
        requireContext().resources.getStringArray(R.array.currency)
    }

    override fun init() {
        initEvents()
        initControls()
    }

    private fun initEvents() {

        settings_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                settings.put(
                    Settings.SettingKey.CURRENCY_UNIT,
                    if (p2 != 0) currencyArr[p2] else ""
                )

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    private fun initControls() {

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currency,
            R.layout.spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            settings_spinner.adapter = arrayAdapter
        }

        var currencyUnit = settings.getString(Settings.SettingKey.CURRENCY_UNIT)
        if (currencyUnit == "") currencyUnit = currencyArr[0]


        settings_spinner.setSelection(currencyArr.binarySearch(currencyUnit))
    }

    override fun getLayoutId(): Int = R.layout.fragment_setting

}