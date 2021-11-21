package com.moony.calc.ui.saving

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.devcomentry.moonlight.binding.BindingFragment
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentSavingDetailBinding
import com.moony.calc.model.Saving
import com.moony.calc.ui.saving.history.SavingHistoryViewModel
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.decimalFormat
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

@AndroidEntryPoint
class SavingDetailFragment(var savings: Saving) :
    BindingFragment<FragmentSavingDetailBinding>(R.layout.fragment_saving_detail) {

    private val savingViewModel: SavingViewModel by activityViewModels()

    private val savingHistoryViewModel: SavingHistoryViewModel by lazy {
        ViewModelProvider(this)[SavingHistoryViewModel::class.java]
    }
    private var moneySavedLiveData: LiveData<Double>? = null
    private var saving: Saving = savings


    override fun initControls(savedInstanceState: Bundle?) {
        savingViewModel.getSaving(saving.idSaving).observe(viewLifecycleOwner, savingObserver)

        moneySavedLiveData = savingHistoryViewModel.getCurrentAmount(saving.idSaving)

        moneySavedLiveData!!.observe(viewLifecycleOwner, moneySavedObserver)

    }

    private val savingObserver = Observer<Saving> { saving ->
        saving?.let {
            this.saving = it
            binding {
                txtSavingDetailDate.text =
                    SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(saving.deadLine)
                txtSavingTotal.text = saving.desiredAmount.decimalFormat()
                (requireActivity() as SavingDetailActivity).supportActionBar?.title = it.title

                Glide.with(this@SavingDetailFragment)
                    .load(AssetFolderManager.assetPath + it.iconUrl)
                    .into(imgSavingDetailCategories)
                txtSavingDetailCategories.text = ""
                if(remainingDays(saving.deadLine) >= 0) {
                    txtSavingRemainingDays.text =
                        remainingDays(saving.deadLine).toString() + " " + getText(R.string.day)
                } else {
                    txtSavingRemainingDays.text = getText(R.string.expired)
                }
            }
        }
    }

    private val moneySavedObserver = Observer<Double> {
        var saved = it
        if (saved == null) saved = 0.0

        var progress = floor(saved / saving.desiredAmount * 100).toInt()
        if (progress > 100) progress = 100

        binding {
            wvSavingDetail.setProgress(progress)
            txtSavingProgress.text = ("$progress%")
            txtSavingSaved.text = saved.decimalFormat()

            var remaining = (saving.desiredAmount - saved)
            txtTitleRemaining.setText(R.string.remaining)
            if (remaining < 0) {
                remaining = (saved - saving.desiredAmount)
                txtTitleRemaining.setText(R.string.redundancy)
            }

            txtSavingRemainingMoney.text = remaining.decimalFormat()
        }
    }

    private fun remainingDays(dueDate: Date): Long {
        val calendar = Calendar.getInstance()
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.HOUR] = 0

        return (dueDate.time - calendar.timeInMillis) / (1000 * 60 * 60 * 24)
    }
}
