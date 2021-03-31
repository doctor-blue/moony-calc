package com.moony.calc.ui.saving

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentSavingDetailBinding
import com.moony.calc.databinding.FragmentTransactionBinding
import com.moony.calc.ui.category.CategoryViewModel
import com.moony.calc.model.Category
import com.moony.calc.model.Saving
import com.moony.calc.ui.saving.history.SavingHistoryViewModel
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.decimalFormat
import kotlin.math.floor

class SavingDetailFragment(var savings: Saving) : BaseFragment() {

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }
    private val savingViewModel: SavingViewModel by lazy {
        ViewModelProvider(this)[SavingViewModel::class.java]
    }
    private val savingHistoryViewModel: SavingHistoryViewModel by lazy {
        ViewModelProvider(this)[SavingHistoryViewModel::class.java]
    }
    private var categoryLiveData: LiveData<Category>? = null
    private var moneySavedLiveData: LiveData<Double>? = null
    private var saving: Saving = savings

    private val binding: FragmentSavingDetailBinding
        get() = (getViewBinding() as FragmentSavingDetailBinding)

    override fun getLayoutId(): Int = R.layout.fragment_saving_detail

    override fun initEvents() {

    }

    override fun initControls(view: View, savedInstanceState: Bundle?) {
        savingViewModel.getSaving(saving.idSaving).observe(viewLifecycleOwner, savingObserver)

        moneySavedLiveData = savingHistoryViewModel.getCurrentAmount(saving.idSaving)

        moneySavedLiveData!!.observe(viewLifecycleOwner, moneySavedObserver)

    }

    private val savingObserver = Observer<Saving> { saving ->
        saving?.let {
            this.saving = it
            binding.txtSavingDetailDate.text = saving.deadLine
            binding.txtSavingTotal.text = saving.desiredAmount.decimalFormat()
            (requireActivity() as SavingDetailActivity).supportActionBar?.title = it.description

            categoryLiveData?.removeObserver(categoryObserver)
            categoryLiveData = categoryViewModel.getCategory(saving.idCategory)
            categoryLiveData!!.observe(viewLifecycleOwner, categoryObserver)
        }
    }
    private val categoryObserver = Observer<Category> {
        Glide.with(this).load(AssetFolderManager.assetPath + it.iconUrl)
            .into(binding.imgSavingDetailCategories)
        binding.txtSavingDetailCategories.text = it.title
    }

    private val moneySavedObserver = Observer<Double> {
        var saved = it
        if (saved == null) saved = 0.0

        var progress = floor(saved / saving.desiredAmount * 100).toInt()
        if (progress > 100) progress = 100

        binding.wvSavingDetail.setProgress(progress)
        binding.txtSavingProgress.text = ("$progress%")
        binding.txtSavingSaved.text = saved.decimalFormat()

        var remaining = (saving.desiredAmount - saved)

        if (remaining < 0) remaining = 0.0

        binding.txtSavingRemaining.text = remaining.decimalFormat()

    }
}
