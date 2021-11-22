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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.devcomentry.moonlight.binding.BindingFragment
import com.moony.calc.R
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
import com.moony.calc.utils.formatDateTime
import com.moony.calc.utils.setAutoHideKeyboard
import com.moony.calc.utils.showDialogConfirm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

abstract class AddTransactionFragmentBase :
    BindingFragment<FragmentAddTransactionBinding>(R.layout.fragment_add_transaction) {

    protected val requestCode = 234

    protected val calendar: Calendar = Calendar.getInstance()

    protected val transactionViewModel: TransactionViewModel by activityViewModels()

    protected var savings: List<Saving> = listOf()

    protected var savingPosition = -1

    protected var category: Category? = null


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

                                edtTransactionMoney.filters =
                                    arrayOf(LengthFilter(maxLength))
                            } else {
                                var maxLength = 9
                                if (it.toString().contains('-')) maxLength++

                                edtTransactionMoney.filters =
                                    arrayOf(LengthFilter(maxLength))
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

            toolbarAddTransaction.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }

            toolbarAddTransaction.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.mnu_save) {
                    checkInput()
                }
                true
            }
            spinSavingGoals.onItemSelectedListener =
                object : AdapterViewBindingAdapter.OnItemSelected,
                    AdapterView.OnItemSelectedListener {
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

            edtTransactionMoney.setAutoHideKeyboard()

            edtTransactionNote.setAutoHideKeyboard()
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

    private fun checkInput() {
        binding {
            val money = edtTransactionMoney.text.toString()
            when {
                money.isEmpty() || handleTextToDouble(
                    (if (money.contains('-')) money.replace('-', ' ').trim() else money)
                ).toDouble() == 0.0 -> {

                    textInputTransactionMoney.error = resources.getString(R.string.empty_error)

                }
                txtTitleTransactionCategory.text.toString().trim().isEmpty() -> {
                    textInputTransactionTitleCategory.error =
                        resources.getString(R.string.empty_category_error)
                }
                else -> {
                    saveTransaction(money)
                }
            }
        }

    }

    protected abstract fun saveTransaction(money: String)

    protected fun handleTextToDouble(s: String): String {
        var text = s
        if (text.contains(',')) {
            text = text.replace(',', '.')
        }
        return text
    }
}

@AndroidEntryPoint
class AddTransactionFragment : AddTransactionFragmentBase() {

    private val savingViewModel: SavingViewModel by lazy {
        ViewModelProvider(this)[SavingViewModel::class.java]
    }
    private val savingHistoryViewModel: SavingHistoryViewModel by lazy {
        ViewModelProvider(this)[SavingHistoryViewModel::class.java]
    }

    override fun saveTransaction(money: String) {
        val balance = arguments?.getString("balance")?.toDouble()

        var description = binding.edtTransactionNote.text.toString()

        if (description.isEmpty() && savingPosition != -1)
            description = requireContext().resources.getString(R.string.add_to) +
                    " " +
                    savings[savingPosition].title +
                    " " +
                    requireContext().resources.getString(R.string.savings)
        val mon = handleTextToDouble(
            (if (money.contains('-')) money.replace('-', ' ').trim() else money)
        ).toDouble()
        val transaction = Transaction(
            mon,
            category!!.idCategory,
            description,
            calendar.time,
        )
        if (!category!!.isIncome && balance != null && balance < mon) {
            requireContext().showDialogConfirm(
                getString(R.string.transaction_greater_than_balance_alert) + " $mon! " + getString(R.string.create_transaction_alert),
                lifecycle,
                {}) {
                confirmCreateTransaction(transaction)
            }
        } else {
            confirmCreateTransaction(transaction)
        }

    }

    private fun confirmCreateTransaction(transaction: Transaction) {
        transactionViewModel.insertTransaction(transaction)
        lifecycleScope.launch {
            if (savingPosition != -1) {
                val savingHistory = SavingHistory(
                    "",
                    savings[savingPosition].idSaving,
                    if (category!!.isIncome) transaction.money * -1 else transaction.money,
                    !category!!.isIncome,
                    calendar.formatDateTime(),
                    transaction.idTransaction
                )
                savingHistoryViewModel.insertSavingHistory(savingHistory)
            }
        }
        requireActivity().onBackPressed()
    }


    override fun initControls(savedInstanceState: Bundle?) {
        binding {
            txtTransactionTime.text = calendar.formatDateTime()

            edtTransactionMoney.setSelection(edtTransactionMoney.text.toString().length)
            val savingAdapter: ArrayAdapter<Saving> =
                ArrayAdapter<Saving>(requireContext(), android.R.layout.simple_list_item_1)
            spinSavingGoals.adapter = savingAdapter

            savingViewModel.getAllSaving().observe(viewLifecycleOwner, {
                savings = it
                savingAdapter.clear()
                savingAdapter.addAll(it)
                savingAdapter.notifyDataSetChanged()

                if (it.isNotEmpty()) {
                    category?.let {
                        if (category!!.resId == R.string.saving) {
                            layoutSavingGoals.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding {
            //Lấy Category về sau khi mở CategoriesActivity để chọn
            if (requestCode == this@AddTransactionFragment.requestCode)
                if (resultCode == Activity.RESULT_OK) {
                    category = data?.getSerializableExtra(MoonyKey.pickCategory) as Category?
                    Glide.with(this@AddTransactionFragment)
                        .load(AssetFolderManager.assetPath + category!!.iconUrl)
                        .into(imgCategories)

                    txtTitleTransactionCategory.text = category!!.title

                    if (savings.isNotEmpty()) {
                        if (category!!.resId == R.string.saving) {
                            layoutSavingGoals.visibility = View.VISIBLE
                            savingPosition = 0
                            spinSavingGoals.setSelection(savingPosition)
                        } else {
                            layoutSavingGoals.visibility = View.GONE
                            savingPosition = -1

                        }
                    }

                    textInputTransactionTitleCategory.error = null

                    var money = edtTransactionMoney.text.toString()

                    if (!category!!.isIncome) {
                        if (edtTransactionMoney.text.toString().isNotEmpty()) {
                            edtTransactionMoney.filters =
                                arrayOf(LengthFilter(money.length + 1))
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
