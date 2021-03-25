package com.moony.calc.ui.transaction

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentAddTransactionBinding
import com.moony.calc.keys.MoonyKey
import com.moony.calc.model.Category
import com.moony.calc.model.Transaction
import com.moony.calc.ui.category.CategoriesActivity
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.formatDateTime
import java.util.*


class AddTransactionFragment : BaseFragment() {
    private var category: Category? = null
    private val requestCode = 234

    private val transactionViewModel: TransactionViewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }
    private val calendar: Calendar = Calendar.getInstance()
    private val settings: Settings by lazy {
        Settings.getInstance(baseContext!!)
    }

    private val binding: FragmentAddTransactionBinding
        get() = (getViewBinding() as FragmentAddTransactionBinding)

    override fun getLayoutId(): Int = R.layout.fragment_add_transaction

    override fun initControls(view: View, savedInstanceState: Bundle?) {

        binding.txtTransactionTime.text = calendar.formatDateTime()

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

                            binding.edtTransactionMoney.filters = arrayOf(LengthFilter(maxLength))
                        } else {
                            var maxLength = 9
                            if (it.toString().contains('-')) maxLength++

                            binding.edtTransactionMoney.filters = arrayOf(LengthFilter(maxLength))
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

        binding.toolbarAddTransaction.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbarAddTransaction.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.mnu_save) {
                saveTransaction()
            }
            true
        }
    }


    private fun saveTransaction() {
        when {

            binding.edtTransactionMoney.text.toString().trim().isEmpty() -> {
                binding.textInputTransactionMoney.error = resources.getString(R.string.empty_error)
            }
            binding.txtTitleTransactionCategory.text.toString().trim().isEmpty() -> {
                binding.textInputTransactionTitleCategory.error =
                    resources.getString(R.string.empty_category_error)
            }
            else -> {

                val money = binding.edtTransactionMoney.text.toString()

                val transaction = Transaction(
                    handleTextToDouble(
                        (if (money.contains('-')) money.replace('-', ' ').trim() else money)
                    ).toDouble(),
                    category!!.idCategory,
                    binding.edtTransactionNote.text.toString(),
                    calendar[Calendar.DAY_OF_MONTH],
                    calendar[Calendar.MONTH],
                    calendar[Calendar.YEAR]
                )
                transactionViewModel.insertTransaction(transaction)
                requireActivity().onBackPressed()

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
        //Lấy Category về sau khi mở CategoriesActivity để chọn
        if (requestCode == this.requestCode)
            if (resultCode == Activity.RESULT_OK) {
                category = data?.getSerializableExtra(MoonyKey.pickCategory) as Category?
                Glide.with(this).load(AssetFolderManager.assetPath + category!!.iconUrl)
                    .into(binding.imgCategories)

                binding.txtTitleTransactionCategory.text = category!!.title
                binding.textInputTransactionTitleCategory.error = null

                var money = binding.edtTransactionMoney.text.toString()

                if (!category!!.isIncome) {
                    if (binding.edtTransactionMoney?.text.toString().isNotEmpty()) {
                        binding.edtTransactionMoney.filters =
                            arrayOf(LengthFilter(money.length + 1))
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

    private fun handleTextToDouble(s: String): String {
        var text = s
        if (text.contains(',')) {
            text = text.replace(',', '.')
        }
        return text
    }


}
