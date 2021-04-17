package com.moony.calc.ui.transaction

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.adapters.AdapterViewBindingAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentAddTransactionBinding
import com.moony.calc.keys.MoonyKey
import com.moony.calc.model.Category
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingHistory
import com.moony.calc.model.Transaction
import com.moony.calc.ui.category.CategoriesActivity
import com.moony.calc.ui.saving.SavingViewModel
import com.moony.calc.ui.saving.history.SavingHistoryViewModel
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.formatDateTime
import com.moony.calc.utils.setAutoHideKeyboard
import kotlinx.coroutines.launch
import java.util.*


class AddTransactionFragment : BaseFragment() {
    private var category: Category? = null
    private val requestCode = 234

    private val transactionViewModel: TransactionViewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }

    private val savingViewModel: SavingViewModel by lazy {
        ViewModelProvider(this)[SavingViewModel::class.java]
    }
    private val savingHistoryViewModel: SavingHistoryViewModel by lazy {
        ViewModelProvider(this)[SavingHistoryViewModel::class.java]
    }

    private val calendar: Calendar = Calendar.getInstance()

    private var savings: List<Saving> = listOf()

    private var savingPosition = -1

    private val settings: Settings by lazy {
        Settings.getInstance(baseContext!!)
    }

    private val binding: FragmentAddTransactionBinding
        get() = (getViewBinding() as FragmentAddTransactionBinding)

    override fun getLayoutId(): Int = R.layout.fragment_add_transaction

    override fun initControls(view: View, savedInstanceState: Bundle?) {

        binding.txtTransactionTime.text = calendar.formatDateTime()

        binding.edtTransactionMoney.setSelection(binding.edtTransactionMoney.text.toString().length)
        val savingAdapter: ArrayAdapter<Saving> =
            ArrayAdapter<Saving>(requireContext(), android.R.layout.simple_list_item_1)
        binding.spinSavingGoals.adapter = savingAdapter

        savingViewModel.getAllSaving().observe(this, {
            savings = it
            savingAdapter.clear()
            savingAdapter.addAll(it)
            savingAdapter.notifyDataSetChanged()

            if (it.isNotEmpty()) {
                category?.let {
                    if (category!!.resId == R.string.saving) {
                        binding.layoutSavingGoals.visibility = View.VISIBLE
                    }
                }
            }
        })
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
        binding.spinSavingGoals.onItemSelectedListener =
            object : AdapterViewBindingAdapter.OnItemSelected, AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    savingPosition = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

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
                var description = binding.edtTransactionNote.text.toString()

                if (description.isEmpty() && savingPosition != -1)
                    description = requireContext().resources.getString(R.string.add_to) +
                            " " +
                            savings[savingPosition].title +
                            " " +
                            requireContext().resources.getString(R.string.savings)

                val transaction = Transaction(
                    handleTextToDouble(
                        (if (money.contains('-')) money.replace('-', ' ').trim() else money)
                    ).toDouble(),
                    category!!.idCategory,
                    description,
                    calendar[Calendar.DAY_OF_MONTH],
                    calendar[Calendar.MONTH],
                    calendar[Calendar.YEAR]
                )
                lifecycleScope.launch {
                    val idTransaction = transactionViewModel.insertTransaction(transaction)
                    if (savingPosition != -1) {
                        val savingHistory = SavingHistory(
                            "",
                            savings[savingPosition].idSaving,
                            handleTextToDouble(
                                (if (money.contains('-')) money.replace('-', ' ').trim() else money)
                            ).toDouble(),
                            !category!!.isIncome,
                            calendar.formatDateTime(),
                            idTransaction.toInt()
                        )
                        savingHistoryViewModel.insertSavingHistory(savingHistory)
                    }
                }
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

                if (category!!.resId == -1) {
                    binding.txtTitleTransactionCategory.text = category!!.title
                } else {
                    binding.txtTitleTransactionCategory.setText(category!!.resId)
                }

                if (savings.isNotEmpty()) {
                    if (category!!.resId == R.string.saving) {
                        binding.layoutSavingGoals.visibility = View.VISIBLE
                    }
                }

                binding.textInputTransactionTitleCategory.error = null

                var money = binding.edtTransactionMoney.text.toString()

                if (!category!!.isIncome) {
                    if (binding.edtTransactionMoney.text.toString().isNotEmpty()) {
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
