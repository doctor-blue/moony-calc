package com.moony.calc.ui.transaction

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.ui.category.CategoriesActivity
import com.moony.calc.base.BaseFragment
import com.moony.calc.ui.dialog.ConfirmDialogBuilder
import com.moony.calc.keys.MoonyKey
import com.moony.calc.model.Category
import com.moony.calc.model.Transaction
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.formatDateTime
import kotlinx.android.synthetic.main.fragment_update_transaction.*
import kotlinx.android.synthetic.main.fragment_update_transaction.edt_transaction_money
import kotlinx.android.synthetic.main.fragment_update_transaction.edt_transaction_note
import kotlinx.android.synthetic.main.fragment_update_transaction.img_categories
import kotlinx.android.synthetic.main.fragment_update_transaction.layout_transaction_category
import kotlinx.android.synthetic.main.fragment_update_transaction.layout_transaction_date_time
import kotlinx.android.synthetic.main.fragment_update_transaction.textInput_transaction_money
import kotlinx.android.synthetic.main.fragment_update_transaction.textInput_transaction_title_category
import kotlinx.android.synthetic.main.fragment_update_transaction.txt_currency_unit
import kotlinx.android.synthetic.main.fragment_update_transaction.txt_title_transaction_category
import kotlinx.android.synthetic.main.fragment_update_transaction.txt_transaction_time
import java.util.*

class UpdateTransactionFragment : BaseFragment() {
    private val requestCode = 234
    private var transaction: Transaction? = null
    private var category: Category? = null

    private val transactionViewModel: TransactionViewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }
    private val calendar: Calendar = Calendar.getInstance()
    private val settings: Settings by lazy {
        Settings.getInstance(baseContext!!)
    }

    override fun init() {
        initControls()
        initEvents()
    }

    private fun initControls() {
        transaction = arguments?.getSerializable(getString(R.string.transaction)) as Transaction
        category = arguments?.getSerializable(getString(R.string.categories)) as Category

        transaction?.let { tran ->
            category?.let { cate ->

                edt_transaction_note.setText(tran.note)

                Glide.with(this).load(AssetFolderManager.assetPath + cate.iconUrl)
                    .into(img_categories)
                txt_title_transaction_category.text = cate.title

                if (category!!.isIncome) {
                    edt_transaction_money.setText(tran.money.decimalFormat())

                } else
                    edt_transaction_money.setText(((-1 * tran.money).decimalFormat()))

                calendar.set(Calendar.DAY_OF_MONTH, tran.day)
                calendar.set(Calendar.MONTH, tran.month)
                calendar.set(Calendar.YEAR, tran.year)
                txt_transaction_time.text = calendar.formatDateTime()

                calendar.set(Calendar.DAY_OF_MONTH, tran.day)
            }
        }
        edt_transaction_money.setSelection(edt_transaction_money.text.toString().length)

        txt_currency_unit.text = settings.getString(
            Settings.SettingKey.CURRENCY_UNIT
        )
    }

    private fun initEvents() {
        layout_transaction_category.setOnClickListener {
            val intent = Intent(requireContext(), CategoriesActivity::class.java)
            startActivityForResult(intent, requestCode)
        }
        btn_delete_transaction.setOnClickListener {
            //Delete Transaction
            transaction?.let { transaction ->
                val builder = ConfirmDialogBuilder(requireContext())
                builder.setContent(resources.getString(R.string.notice_delete_transaction))
                val dialog = builder.createDialog()
                builder.btnConfirm.setOnClickListener {
                    transactionViewModel.deleteTransaction(transaction)
                    dialog.dismiss()
                    requireActivity().onBackPressed()
                    requireActivity().onBackPressed()
                }
                builder.btnCancel.setOnClickListener { dialog.dismiss() }
                builder.showDialog()

            }
        }

        layout_transaction_date_time.setOnClickListener {
            pickDateTime()
        }


        edt_transaction_money.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                s?.let {
                    if (s.isNotEmpty()) {
                        textInput_transaction_money.error = null
                        if (it.toString().contains('.') || it.toString().contains(',')) {
                            var maxLength = 11

                            if (it.toString().contains('-')) maxLength++

                            edt_transaction_money.filters = arrayOf(
                                InputFilter.LengthFilter(
                                    maxLength
                                )
                            )
                        } else {
                            var maxLength = 9
                            if (it.toString().contains('-')) maxLength++

                            edt_transaction_money.filters = arrayOf(
                                InputFilter.LengthFilter(
                                    maxLength
                                )
                            )
                            if (it.length - 1 == maxLength - 1) {
                                //kiểm tra nếu kí tự cuối cùng không là . or , thì xóa kí tự đó đi
                                val lastChar = it[maxLength - 1]
                                if (!(lastChar == '.' || lastChar == ',')) {
                                    edt_transaction_money.setText(it.subSequence(0, maxLength - 1))
                                    edt_transaction_money.setSelection(maxLength - 1)
                                }
                            }
                        }

                    }
                }
            }

        })

        toolbar_update_transaction.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        toolbar_update_transaction.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.mnu_save)
                saveTransaction()

            true
        }
    }


    private fun saveTransaction() {
        when {

            edt_transaction_money.text.toString().trim().isEmpty() -> {
                textInput_transaction_money.error = resources.getString(R.string.empty_error)
            }
            txt_title_transaction_category.text.toString().trim().isEmpty() -> {
                textInput_transaction_title_category.error =
                    resources.getString(R.string.empty_category_error)
            }
            else -> {
                val money = edt_transaction_money.text.toString()

                transaction?.let { transaction ->
                    transaction.day = calendar[Calendar.DAY_OF_MONTH]
                    transaction.note = edt_transaction_note.text.toString()
                    transaction.money = handleTextToDouble(
                        (if (money.contains('-')) money.replace('-', ' ').trim() else money)
                    ).toDouble()
                    transaction.month = calendar[Calendar.MONTH]
                    transaction.year = calendar[Calendar.YEAR]
                    transaction.idCategory = category!!.idCategory
                    transaction.isIncome = category!!.isIncome

                    transactionViewModel.updateTransaction(transaction)
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

            txt_transaction_time.text = calendar.formatDateTime()

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
                    .into(img_categories)

                txt_title_transaction_category.text = category!!.title
                textInput_transaction_title_category.error = null

                var money = edt_transaction_money.text.toString()

                if (!category!!.isIncome) {
                    if (edt_transaction_money?.text.toString().isNotEmpty()) {
                        edt_transaction_money.filters =
                            arrayOf(InputFilter.LengthFilter(money.length + 1))
                    }
                    if (!money.contains('-'))
                        money = "-$money"
                } else {
                    if (money.contains('-'))
                        money=money.replace('-', ' ').trim()
                }
                edt_transaction_money.setText(money)

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