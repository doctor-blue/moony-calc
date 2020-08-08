package com.moony.calc.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.activities.CategoriesActivity
import com.moony.calc.base.BaseFragment
import com.moony.calc.database.TransactionViewModel
import com.moony.calc.keys.MoonyKey
import com.moony.calc.model.Category
import com.moony.calc.model.Transaction
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.formatDateTime
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import java.util.*


class AddTransactionFragment : BaseFragment() {
    private var category: Category? = null
    private val requestCode = 234

    private val transactionViewModel: TransactionViewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }
    private val calendar: Calendar = Calendar.getInstance()


    override fun init() {
        initControls()
        initEvents()
    }

    override fun getLayoutId(): Int = R.layout.fragment_add_transaction

    private fun initControls() {

        txt_transaction_time.text = calendar.formatDateTime()

        edt_transaction_money.setSelection(edt_transaction_money.text.toString().length)


    }


    private fun initEvents() {
        layout_transaction_category.setOnClickListener {
            val intent = Intent(requireContext(), CategoriesActivity::class.java)
            startActivityForResult(intent, requestCode)
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
                            /**
                             * "${handleTextToDouble(it.toString())}0" nhiều trường hợp nó ở dạng #. thay vì #.# nên thêm 0 để đc chuỗi dạng #.0
                             * chuyển về kiểu double sau đó format nó sẽ được chuỗi ở dạng #.00 và lấy độ dài làm max length cho edit_text
                             */
                            val maxLength = "${handleTextToDouble(it.toString())}1".toDouble()
                                .decimalFormat().length
                            edt_transaction_money.filters = arrayOf(LengthFilter(maxLength))
                        } else {
                            edt_transaction_money.filters = arrayOf(LengthFilter(9))
                            if (it.length - 1 == 8) {
                                //kiểm tra nếu kí tự cuối cùng không là . or , thì xóa kí tự đó đi
                                val lastChar = it[8]
                                if (!(lastChar == '.' || lastChar == ',')) {
                                    edt_transaction_money.setText(it.subSequence(0, 8))
                                    edt_transaction_money.setSelection(8)
                                }
                            }
                        }
                    }
                }

            }

        })

        toolbar_add_transaction.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        toolbar_add_transaction.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.mnu_save) {
                saveTransaction()
            }
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
                val transaction = Transaction(
                    handleTextToDouble(edt_transaction_money.text.toString()).toDouble(),
                    category!!.isIncome,
                    category!!.idCategory,
                    edt_transaction_note.text.toString(),
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

                if (category!!.isIncome) {
                    edt_transaction_money.setTextColor(resources.getColor(R.color.blue))
                } else {
                    edt_transaction_money.setTextColor(resources.getColor(R.color.colorAccent))
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
