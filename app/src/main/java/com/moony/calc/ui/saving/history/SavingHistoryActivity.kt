package com.moony.calc.ui.saving.history

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.databinding.ActivitySavingHistoryBinding
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingHistory
import com.moony.calc.model.Transaction
import com.moony.calc.ui.category.CategoryViewModel
import com.moony.calc.ui.transaction.TransactionViewModel
import com.moony.calc.utils.decimalFormat
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class SavingHistoryActivity : BaseActivity() {
    private val binding: ActivitySavingHistoryBinding
        get() = (getViewBinding() as ActivitySavingHistoryBinding)

    private var calendar = Calendar.getInstance()
    private var dateAdded = ""
    private var isSaving = true
    private var saving: Saving? = null
    private var savingHistory: SavingHistory? = null

    private val savingHistoryViewModel: SavingHistoryViewModel by lazy {
        ViewModelProvider(this)[SavingHistoryViewModel::class.java]
    }

    private val transactionViewModel: TransactionViewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }

    override fun getLayoutId(): Int = R.layout.activity_saving_history


    override fun initControls(savedInstanceState: Bundle?) {
        saving = intent.getSerializableExtra(SavingHistoryFragment.SAVING) as Saving
        isSaving = intent.getBooleanExtra(SavingHistoryFragment.IS_SAVING, true)
        savingHistory =
            intent.getSerializableExtra(SavingHistoryFragment.EDIT_HISTORY) as SavingHistory?

        savingHistory?.let {
            isSaving = it.isSaving

            binding.edtHistorySavingDescription.setText(it.description)

            binding.edtSavingHistoryAmount.setText((it.amount.toString()))
            dateAdded = it.date
            binding.txtDueDate.text = (resources.getString(R.string.date) + " " + dateAdded)
        }

        if (isSaving)
            binding.toolbarHistorySaving.title = resources.getString(R.string.more_money_in)
        else
            binding.toolbarHistorySaving.title = resources.getString(R.string.get_money_out)




        setSupportActionBar(binding.toolbarHistorySaving)
    }

    override fun initEvents() {
        binding.edtSavingHistoryAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                s?.let {
                    if (it.isNotEmpty()) {
                        binding.tipHistorySavingAmount.error = null
                        if (it.toString().contains('.') || it.toString().contains(',')) {
                            /**
                             * "${handleTextToDouble(it.toString())}0" nhiều trường hợp nó ở dạng #. thay vì #.# nên thêm 0 để đc chuỗi dạng #.0
                             * chuyển về kiểu double sau đó format nó sẽ được chuỗi ở dạng #.00 và lấy độ dài làm max length cho edit_text
                             */
                            val maxLength = "${handleTextToDouble(it.toString())}1".toDouble()
                                .decimalFormat().length
                            binding.edtSavingHistoryAmount.filters =
                                arrayOf(InputFilter.LengthFilter(maxLength))
                        } else {
                            binding.edtSavingHistoryAmount.filters =
                                arrayOf(InputFilter.LengthFilter(9))
                            if (it.length - 1 == 8) {
                                //kiểm tra nếu kí tự cuối cùng không là . or , thì xóa kí tự đó đi
                                val lastChar = it[8]
                                if (!(lastChar == '.' || lastChar == ',')) {
                                    binding.edtSavingHistoryAmount.setText(it.subSequence(0, 8))
                                    binding.edtSavingHistoryAmount.setSelection(8)
                                }
                            }
                        }
                    }
                }
            }
        })

        binding.edtHistorySavingDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtHistorySavingDescription.text.toString().trim().isNotEmpty()) {
                    binding.tipHistorySavingDescription.error = null
                }
            }
        })

        binding.layoutSavingHistoryCalendar.setOnClickListener {
            pickDateTime()
        }

        binding.toolbarHistorySaving.setNavigationOnClickListener { finish() }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mnu_save) {
            saveHistory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveHistory() {
        val snackbar: Snackbar =
            Snackbar.make(
                binding.layoutRootSavingHistory,
                R.string.empty_date_error,
                Snackbar.LENGTH_LONG
            )
        snackbar.setTextColor(resources.getColor(R.color.white))
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
        snackbar.animationMode = Snackbar.ANIMATION_MODE_SLIDE


        when {
            binding.edtSavingHistoryAmount.text.toString().trim().isEmpty() -> {
                binding.tipHistorySavingAmount.error = resources.getString(R.string.empty_error)
            }
//            binding.edtHistorySavingDescription.text.toString().trim().isEmpty() -> {
//                binding.tipHistorySavingDescription.error =
//                    resources.getString(R.string.empty_error)
//            }
            binding.txtDueDate.text.toString().trim().isEmpty() -> {
                snackbar.show()
            }
            else -> {
                if (savingHistory != null) {
                    var amount = binding.edtSavingHistoryAmount.text.toString().toDouble()
                    if (!isSaving) amount *= -1

                    savingHistory!!.amount = amount
                    savingHistory!!.isSaving = isSaving
                    savingHistory!!.date = dateAdded
                    savingHistory!!.description =
                        binding.edtHistorySavingDescription.text.toString()

                    savingHistoryViewModel.updateSavingHistory(savingHistory!!)
                    finish()

                } else {
                    var amount = binding.edtSavingHistoryAmount.text.toString().toDouble()
                    if (!isSaving) amount *= -1

                    var description = binding.edtHistorySavingDescription.text.toString()

                    if (description.isEmpty())
                        description = if (isSaving) {
                            resources.getString(R.string.add_to) +
                                    " " +
                                    saving!!.title +
                                    " " +
                                    resources.getString(R.string.savings)
                        } else {
                            resources.getString(R.string.take_money_out_of_savings) +
                                    " " +
                                    saving!!.title
                        }
                    categoryViewModel.getSavingCategory(!isSaving, R.string.saving)
                        .observe(this, {
                            lifecycleScope.launch {
                                val transaction = Transaction(
                                    abs(amount),
                                    it.idCategory,
                                    description,
                                    calendar[Calendar.DAY_OF_MONTH],
                                    calendar[Calendar.MONTH],
                                    calendar[Calendar.YEAR],
                                )
                                val idTransaction =
                                    transactionViewModel.insertTransaction(transaction)

                                val savingHistory = SavingHistory(
                                    description,
                                    saving!!.idSaving,
                                    amount,
                                    isSaving,
                                    dateAdded,
                                    idTransaction.toInt()
                                )
                                savingHistoryViewModel.insertSavingHistory(savingHistory)
                                finish()
                            }
                        })


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

    @SuppressLint("SetTextI18n")
    private fun pickDateTime() {
        val dialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            dateAdded = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.time)
            binding.txtDueDate.text = resources.getString(R.string.date) + " " + dateAdded
        }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])
        dialog.show()
    }
}