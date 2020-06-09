package com.moony.calc.fragments

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.database.CategoryViewModel
import com.moony.calc.model.Saving
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.decimalFormat
import kotlinx.android.synthetic.main.fragment_saving_detail.*

class SavingDetailFragment(var saving: Saving) : BaseFragment() {

    val categoryViewModel: CategoryViewModel by lazy { ViewModelProvider(this)[CategoryViewModel::class.java] }

    override fun getLayoutId(): Int = R.layout.fragment_saving_detail
    override fun init() {
        initControl()
        initEvent()

    }

    private fun initEvent() {

    }

    private fun initControl() {
        wv_saving_detail.setProgress(55)
        txt_saving_detail_date.text = saving.deadLine
        txt_saving_total.text = saving.desiredAmount.decimalFormat()
        categoryViewModel.getCategory(saving.idCategory).observe(this, Observer {
            Glide.with(this).load(AssetFolderManager.assetPath + it.iconUrl).into(img_saving_detail_categories)
            txt_saving_detail_categories.text = it.title
        })


    }
}
