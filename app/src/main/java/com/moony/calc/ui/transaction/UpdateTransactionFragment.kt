package com.moony.calc.ui.transaction

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.devcomentry.moonlight.binding.BindingFragment
import com.moony.calc.R
import com.moony.calc.databinding.FragmentUpdateTransactionBinding
import com.moony.calc.keys.MoonyKey
import com.moony.calc.model.Category
import com.moony.calc.model.TransactionItem
import com.moony.calc.ui.category.CategoriesActivity
import com.moony.calc.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateTransactionFragment :
    BindingFragment<FragmentUpdateTransactionBinding>(R.layout.fragment_update_transaction) {
    private val requestCode = 234
    private var transactionItem: TransactionItem? = null

    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private val calendar: Calendar = Calendar.getInstance()


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

    override fun initEvents() {
        binding {
            layoutTransactionCategory.setOnClickListener {
                val intent = Intent(requireContext(), CategoriesActivity::class.java)
                startActivityForResult(intent, requestCode)
            }


            layoutTransactionDateTime.setOnClickListener {
                pickDateTime()
            }


            edtTransactionMoney.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) =
                    Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                    s?.let {
                        if (s.isNotEmpty()) {
                            textInputTransactionMoney.error = null
                            if (it.toString().contains('.') || it.toString().contains(',')) {
                                var maxLength = 11

                                if (it.toString().contains('-')) maxLength++

                                edtTransactionMoney.filters = arrayOf(
                                    InputFilter.LengthFilter(
                                        maxLength
                                    )
                                )
                            } else {
                                var maxLength = 9
                                if (it.toString().contains('-')) maxLength++

                                edtTransactionMoney.filters = arrayOf(
                                    InputFilter.LengthFilter(
                                        maxLength
                                    )
                                )
                                if (it.length - 1 == maxLength - 1) {
                                    //kiểm tra nếu kí tự cuối cùng không là . or , thì xóa kí tự đó đi
                                    val lastChar = it[maxLength - 1]
                                    if (!(lastChar == '.' || lastChar == ',')) {
                                        edtTransactionMoney.setText(
                                            it.subSequence(
                                                0,
                                                maxLength - 1
                                            )
                                        )
                                        edtTransactionMoney.setSelection(maxLength - 1)
                                    }
                                }
                            }

                        }
                    }
                }

            })

            toolbarUpdateTransaction.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }

            toolbarUpdateTransaction.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.mnu_save)
                    saveTransaction()

                true
            }
            edtTransactionMoney.setAutoHideKeyboard()
            edtTransactionNote.setAutoHideKeyboard()
        }
    }


    private fun saveTransaction() {
        binding {
            val money = edtTransactionMoney.text.toString()
            when {
                money.isEmpty() || handleTextToDouble(
                    (if (money.contains('-')) money.replace('-', ' ').trim() else money)
                ).toDouble() == 0.0 -> {

                    textInputTransactionMoney.error =
                        resources.getString(R.string.empty_error)


                }
                txtTitleTransactionCategory.text.toString().trim().isEmpty() -> {
                    textInputTransactionTitleCategory.error =
                        resources.getString(R.string.empty_category_error)
                }
                else -> {
                    transactionItem?.let { item ->
                        item.transaction.transactionTime = calendar.time
                        item.transaction.note = edtTransactionNote.text.toString()
                        item.transaction.money = handleTextToDouble(
                            (if (money.contains('-')) money.replace('-', ' ').trim() else money)
                        ).toDouble()

                        transactionViewModel.updateTransaction(item.transaction)
                    }
                    requireActivity().onBackPressed()

                }
            }
        }
    }

    private fun pickDateTime() {
        val dialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            binding.txtTransactionTime.text = calendar.formatDateTime()

        }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])
        dialog.show()

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

    private fun handleTextToDouble(s: String): String {
        var text = s
        if (text.contains(',')) {
            text = text.replace(',', '.')
        }
        return text
    }
}