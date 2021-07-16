package com.moony.calc.ui.transaction

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.keys.MoonyKey
import com.moony.calc.model.Category
import com.moony.calc.model.TransactionItem
import com.moony.calc.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateTransactionFragment :
    AddTransactionFragmentBase(){
    private var transactionItem: TransactionItem? = null


    override fun initControls(savedInstanceState: Bundle?) {
        transactionItem =
            arguments?.getSerializable(getString(R.string.transaction)) as TransactionItem

        transactionItem?.let { item ->

            binding {
                edtTransactionNote.setText(item.transaction.note)

                Glide.with(this@UpdateTransactionFragment)
                    .load(AssetFolderManager.assetPath + item.category.iconUrl)
                    .into(imgCategories)

                if (item.category.resId != -1) {
                    txtTitleTransactionCategory.setText(item.category.resId)
                } else {
                    txtTitleTransactionCategory.text = item.category.title

                }

                if (item.category.isIncome) {
                    edtTransactionMoney.setText(item.transaction.money.decimalFormat())

                } else
                    edtTransactionMoney.setText(((-1 * item.transaction.money).decimalFormat()))

                calendar.time = item.transaction.transactionTime
                txtTransactionTime.text = calendar.formatDateTime()

                edtTransactionMoney.setSelection(edtTransactionMoney.text.toString().length)

            }
        }
    }

    override fun saveTransaction(money: String) {
        transactionItem?.let { item ->
            item.transaction.transactionTime = calendar.time
            item.transaction.note = binding.edtTransactionNote.text.toString()
            item.transaction.money = handleTextToDouble(
                (if (money.contains('-')) money.replace('-', ' ').trim() else money)
            ).toDouble()

            transactionViewModel.updateTransaction(item.transaction)
        }
        requireActivity().onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding {
            //Lấy Category về sau khi mở CategoriesActivity để chọn
            if (requestCode == this@UpdateTransactionFragment.requestCode)
                if (resultCode == Activity.RESULT_OK) {

                    transactionItem?.let { item ->
                        item.category =
                            data?.getSerializableExtra(MoonyKey.pickCategory) as Category
                        item.transaction.idCategory = item.category.idCategory

                        Glide.with(this@UpdateTransactionFragment)
                            .load(AssetFolderManager.assetPath + item.category.iconUrl)
                            .into(imgCategories)

                        if (item.category.resId != -1) {
                            txtTitleTransactionCategory.setText(item.category.resId)
                        } else {
                            txtTitleTransactionCategory.text = item.category.title

                        }

                        textInputTransactionTitleCategory.error = null

                        var money = edtTransactionMoney.text.toString()

                        if (transactionItem?.category!!.isIncome) {
                            if (edtTransactionMoney.text.toString().isNotEmpty()) {
                                edtTransactionMoney.filters =
                                    arrayOf(InputFilter.LengthFilter(money.length + 1))
                            }
                            if (!money.contains('-'))
                                money = "-$money"
                        } else {
                            if (money.contains('-'))
                                money = money.replace('-', ' ').trim()
                        }
                        edtTransactionMoney.setText(money)
                    }


                }
        }
    }

}