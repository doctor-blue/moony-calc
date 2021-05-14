package com.moony.calc.ui.transaction

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentUpdateTransactionBinding
import com.moony.calc.keys.MoonyKey
import com.moony.calc.model.Category
import com.moony.calc.model.TransactionItem
import com.moony.calc.ui.category.CategoriesActivity
import com.moony.calc.ui.dialog.ConfirmDialogBuilder
import com.moony.calc.utils.*
import java.util.*

class UpdateTransactionFragment : BaseFragment() {
    private val requestCode = 234
    private var transactionItem: TransactionItem? = null

    private val transactionViewModel: TransactionViewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }
    private val calendar: Calendar = Calendar.getInstance()
    private val settings: Settings by lazy {
        Settings.getInstance(baseContext!!)
    }
    private val binding: FragmentUpdateTransactionBinding
        get() = (getViewBinding() as FragmentUpdateTransactionBinding)


    override fun initControls(view: View, savedInstanceState: Bundle?) {
        transactionItem =
            arguments?.getSerializable(getString(R.string.transaction)) as TransactionItem

        transactionItem?.let { item ->

            binding.edtTransactionNote.setText(item.transaction.note)

            Glide.with(this).load(AssetFolderManager.assetPath + item.category.iconUrl)
                .into(binding.imgCategories)
            if (item.category.resId != -1) {
                binding.txtTitleTransactionCategory.setText(item.category.resId)
            } else {
                binding.txtTitleTransactionCategory.text = item.category.title

            }

            if (item.category.isIncome) {
                binding.edtTransactionMoney.setText(item.transaction.money.decimalFormat())

            } else
                binding.edtTransactionMoney.setText(((-1 * item.transaction.money).decimalFormat()))

            calendar.time = item.transaction.transactionTime
            binding.txtTransactionTime.text = calendar.formatDateTime()

        }
        binding.edtTransactionMoney.setSelection(binding.edtTransactionMoney.text.toString().length)

        binding.txtCurrencyUnit.text = settings.getString(
            Settings.SettingKey.CURRENCY_UNIT
        )
    }

    override fun initEvents() {
        binding.layoutTransactionCategory.setOnClickListener {
            val intent = Intent(requireContext(), CategoriesActivity::class.java)
            startActivityForResult(intent, requestCode)
        }

        binding.btnDeleteTransaction.setOnClickListener {
            //Delete Transaction
            transactionItem?.let { item ->
                val builder = ConfirmDialogBuilder(requireContext())
                builder.setContent(resources.getString(R.string.notice_delete_transaction))
                val dialog = builder.createDialog()
                builder.btnConfirm.setOnClickListener {
                    transactionViewModel.deleteTransaction(item.transaction)
                    dialog.dismiss()
                    requireActivity().onBackPressed()
                    requireActivity().onBackPressed()
                }
                builder.btnCancel.setOnClickListener { dialog.dismiss() }
                builder.showDialog()

            }
        }

        binding.layoutTransactionDateTime.setOnClickListener {
            pickDateTime()
        }


        binding.edtTransactionMoney.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                s?.let {
                    if (s.isNotEmpty()) {
                        binding.textInputTransactionMoney.error = null
                        if (it.toString().contains('.') || it.toString().contains(',')) {
                            var maxLength = 11

                            if (it.toString().contains('-')) maxLength++

                            binding.edtTransactionMoney.filters = arrayOf(
                                InputFilter.LengthFilter(
                                    maxLength
                                )
                            )
                        } else {
                            var maxLength = 9
                            if (it.toString().contains('-')) maxLength++

                            binding.edtTransactionMoney.filters = arrayOf(
                                InputFilter.LengthFilter(
                                    maxLength
                                )
                            )
                            if (it.length - 1 == maxLength - 1) {
                                //kiểm tra nếu kí tự cuối cùng không là . or , thì xóa kí tự đó đi
                                val lastChar = it[maxLength - 1]
                                if (!(lastChar == '.' || lastChar == ',')) {
                                    binding.edtTransactionMoney.setText(
                                        it.subSequence(
                                            0,
                                            maxLength - 1
                                        )
                                    )
                                    binding.edtTransactionMoney.setSelection(maxLength - 1)
                                }
                            }
                        }

                    }
                }
            }

        })

        binding.toolbarUpdateTransaction.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbarUpdateTransaction.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.mnu_save)
                saveTransaction()

            true
        }
        binding.edtTransactionMoney.setAutoHideKeyboard()
        binding.edtTransactionNote.setAutoHideKeyboard()
    }


    private fun saveTransaction() {
        val money = binding.edtTransactionMoney.text.toString()
        when {
            money.isEmpty() || handleTextToDouble(
                (if (money.contains('-')) money.replace('-', ' ').trim() else money)
            ).toDouble() == 0.0 -> {

                binding.textInputTransactionMoney.error = resources.getString(R.string.empty_error)


            }
            binding.txtTitleTransactionCategory.text.toString().trim().isEmpty() -> {
                binding.textInputTransactionTitleCategory.error =
                    resources.getString(R.string.empty_category_error)
            }
            else -> {
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
        }

    }

    override fun getLayoutId(): Int = R.layout.fragment_update_transaction

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
        //Lấy Category về sau khi mở CategoriesActivity để chọn
        if (requestCode == this.requestCode)
            if (resultCode == Activity.RESULT_OK) {

                transactionItem?.let { item ->
                    item.category =
                        data?.getSerializableExtra(MoonyKey.pickCategory) as Category
                    item.transaction.idCategory = item.category.idCategory

                    Glide.with(this).load(AssetFolderManager.assetPath + item.category.iconUrl)
                        .into(binding.imgCategories)

                    if (item.category.resId != -1) {
                        binding.txtTitleTransactionCategory.setText(item.category.resId)
                    } else {
                        binding.txtTitleTransactionCategory.text = item.category.title

                    }

                    binding.textInputTransactionTitleCategory.error = null

                    var money = binding.edtTransactionMoney.text.toString()

                    if (transactionItem?.category!!.isIncome) {
                        if (binding.edtTransactionMoney.text.toString().isNotEmpty()) {
                            binding.edtTransactionMoney.filters =
                                arrayOf(InputFilter.LengthFilter(money.length + 1))
                        }
                        if (!money.contains('-'))
                            money = "-$money"
                    } else {
                        if (money.contains('-'))
                            money = money.replace('-', ' ').trim()
                    }
                    binding.edtTransactionMoney.setText(money)
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