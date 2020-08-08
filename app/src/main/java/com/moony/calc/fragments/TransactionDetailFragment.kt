package com.moony.calc.fragments

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.database.CategoryViewModel
import com.moony.calc.model.Category
import com.moony.calc.model.Transaction
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.formatDateTime
import kotlinx.android.synthetic.main.fragment_transaction_detail.*
import java.util.*

class TransactionDetailFragment : BaseFragment() {
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }
    private var calendar = Calendar.getInstance()

    override fun init() {
        initControls()
        initEvents()
    }

    private fun initControls() {
        val transaction = arguments?.getSerializable(getString(R.string.transaction)) as Transaction
        val category = arguments?.getSerializable(getString(R.string.categories)) as Category
        txt_transaction_money.text = transaction.money.decimalFormat()
        txt_transaction_note.text = transaction.note

        Glide.with(this).load(AssetFolderManager.assetPath + category.iconUrl)
            .into(img_category)
        txt_category_title.text = category.title

        if (category.isIncome) {

        } else {

        }

        calendar.set(Calendar.DAY_OF_MONTH, transaction.day)
        calendar.set(Calendar.MONTH, transaction.month)
        calendar.set(Calendar.YEAR, transaction.year)
        txt_transaction_date.text = calendar.formatDateTime()

        calendar.set(Calendar.DAY_OF_MONTH, transaction.day)
    }


    private fun initEvents() {
        btn_update_transaction.setOnClickListener {
            findNavController().navigate(R.id.action_transactionDetailFragment_to_updateTransactionFragment)
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_transaction_detail

}