package com.moony.calc.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentSettingBinding
import com.moony.calc.utils.Settings

class SettingFragment : BaseFragment() {

    private val settings: Settings by lazy {
        Settings.getInstance(requireContext())
    }
    private val currencyArr: Array<String> by lazy {
        requireContext().resources.getStringArray(R.array.currency)
    }
    private val binding: FragmentSettingBinding
        get() = (getViewBinding() as FragmentSettingBinding)


    override fun initEvents() {

        binding.settingsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    override fun initControls(view: View, savedInstanceState: Bundle?) {

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currency,
            R.layout.spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.settingsSpinner.adapter = arrayAdapter
        }

        var currencyUnit = settings.getString(Settings.SettingKey.CURRENCY_UNIT)
        if (currencyUnit == "") currencyUnit = currencyArr[0]


        binding.settingsSpinner.setSelection(currencyArr.binarySearch(currencyUnit))
    }

    override fun getLayoutId(): Int = R.layout.fragment_setting

}