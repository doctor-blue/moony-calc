package com.moony.calc.ui.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.devcomentry.moonlight.binding.BindingFragment
import com.moony.calc.R
import com.moony.calc.databinding.FragmentSettingBinding
import com.moony.calc.ui.category.CategoriesActivity
import com.moony.calc.utils.Settings
import com.moony.calc.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingFragment : BindingFragment<FragmentSettingBinding>(R.layout.fragment_setting),
    View.OnClickListener {

    private val settings: Settings by lazy {
        Settings.getInstance(requireContext())
    }
    private val currencyArr: Array<String> by lazy {
        requireContext().resources.getStringArray(R.array.currency)
    }


    override fun initEvents() {
        binding {

            settingsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    settings.put(
                        Settings.SettingKey.CURRENCY_UNIT,
                        if (p2 != 0) currencyArr[p2] else ""
                    )

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

            layoutInviteFriend.setOnClickListener(this@SettingFragment)
            layoutFeedback.setOnClickListener(this@SettingFragment)
            layoutRateUs.setOnClickListener(this@SettingFragment)
            layoutAbout.setOnClickListener(this@SettingFragment)
            layoutForDev.setOnClickListener(this@SettingFragment)
            layoutCategory.setOnClickListener(this@SettingFragment)
        }


    }

    override fun initControls(savedInstanceState: Bundle?) {

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

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.layout_invite_friend -> {
                val clipboard: ClipboardManager? =
                    requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
                val clip: ClipData = ClipData.newPlainText(
                    "",
                    "http://play.google¬.com/store/apps/details?id=${requireContext().packageName}"
                )
                clipboard!!.setPrimaryClip(clip)
                requireContext().showToast(R.string.copied_link_to_clipbroad)
            }

            R.id.layout_feedback -> {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("devcomentry@gmail.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Moony Feedback")
                startActivity(Intent.createChooser(intent, "Send Email"))
            }

            R.id.layout_rate_us -> {
                openLink("http://play.google.com/store/apps/details?id=${requireContext().packageName}")
            }

            R.id.layout_about -> {
                openLink("https://github.com/doctor-blue/moony")
            }

            R.id.layout_for_dev -> {
                openLink("https://github.com/doctor-blue/moony-documentation/blob/master/DEVELOPER.md#some-useful-information-if-you-are-a-developer")
            }

            R.id.layout_category -> {
                val intent = Intent(requireContext(), CategoriesActivity::class.java)
                intent.putExtra(CategoriesActivity.KEY, true)
                startActivity(intent)
            }
        }
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}