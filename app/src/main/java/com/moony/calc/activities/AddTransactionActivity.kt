package com.moony.calc.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.database.DateTimeViewModel
import com.moony.calc.database.TransactionViewModel
import com.moony.calc.keys.MoonyKey
import com.moony.calc.model.Category
import com.moony.calc.model.DateTime
import com.moony.calc.model.Transaction
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.formatDateTime
import kotlinx.android.synthetic.main.activity_add_transaction.*
import java.util.*


class AddTransactionActivity : BaseActivity() {
    private var isDetails: Boolean = false
    private var transaction: Transaction? = null
    private var dateTime: DateTime? = null
    private var category: Category? = null
    private val requestCode = 234
    private val transactionViewModel: TransactionViewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }
    private val calendar: Calendar = Calendar.getInstance()

    private val dateTimeViewModel: DateTimeViewModel by lazy {
        ViewModelProvider(this)[DateTimeViewModel::class.java]
    }

    companion object {
        const val KEYS = "CATEGORY_ADD_TRANSACTION"
    }

    override fun init(savedInstanceState: Bundle?) {
        initControls()
        initEvents()
    }

    override fun getLayoutId(): Int = R.layout.activity_add_transaction

    private fun initControls() {
        setSupportActionBar(toolbar_add_transaction)
        val intent = intent
        transaction =
            intent.getSerializableExtra(MoonyKey.transactionDetail) as Transaction?
        dateTime =
            intent.getSerializableExtra(MoonyKey.transactionDateTime) as DateTime?
        category =
            intent.getSerializableExtra(MoonyKey.transactionCategory) as Category?

        txt_transaction_time.text = calendar.formatDateTime()

        transaction?.let { tran ->
            //transaction,category và date time không = null tức là đang ở trạng thái detail nên set thông tin cho các view
            category?.let { cate ->
                dateTime?.let { date ->
                    isDetails = true
                    toolbar_add_transaction.title = resources.getString(R.string.transaction_detail)

                    edt_transaction_money.setText(tran.money.decimalFormat())
                    edt_transaction_note.setText(tran.note)

                    //Glide.with(this).load(cate.iconUrl).into(img_categories)
                    txt_title_transaction_category.text = cate.title

                    calendar.set(Calendar.DAY_OF_MONTH, date.day)
                    calendar.set(Calendar.MONTH, date.month)
                    calendar.set(Calendar.YEAR, date.year)
                    txt_transaction_time.text = calendar.formatDateTime()

                    calendar.set(Calendar.DAY_OF_MONTH, date.day)
                    btn_delete_transaction.visibility = View.VISIBLE
                }
            }
        }


    }


    private fun initEvents() {
        layout_transaction_category.setOnClickListener {
            val intent = Intent(this@AddTransactionActivity, CategoriesActivity::class.java)
            startActivityForResult(intent, requestCode)
        }
        btn_delete_transaction.setOnClickListener {
            //Delete Transaction
            transaction?.let {
                transactionViewModel.deleteTransaction(it)
                finish()
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
                    if (it.toString().contains('.') || it.toString().contains(',')) {
                        /**
                         * "${handleTextToDouble(it.toString())}0" nhiều trường hợp nó ở dạng #. thay vì #.# nên thêm 0 để đc chuỗi dạng #.0
                         * chuyển về kiểu double sau đó format nó sẽ được chuỗi ở dạng #.00 và lấy độ dài làm max length cho edit_text
                         */
                        val maxLength = "${handleTextToDouble(it.toString())}0".toDouble().decimalFormat().length
                        edt_transaction_money.filters = arrayOf(LengthFilter(maxLength))
                    } else {
                        edt_transaction_money.filters = arrayOf(LengthFilter(9))
                        if (it.length - 1 == 8) {
                            //kiểm tra nếu kí tự cuối cùng không là . or , thì xóa kí tự đó đi
                            val lastChar = it[8]
                            if (!(lastChar == '.' || lastChar == ',')) {
                                edt_transaction_money.setText(it.subSequence(0, 8))
                            }
                        }
                    }
                }

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mnu_save) {
            saveTransaction()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveTransaction() {
        //Sẽ lắng nghe coi date time đã được thêm vào database hay chưa để có thể get id của date time ra và thêm transaction hoặc update transaction
        dateTimeViewModel.getDateTime(
            calendar[Calendar.DAY_OF_MONTH],
            calendar[Calendar.MONTH],
            calendar[Calendar.YEAR]
        )
            .observe(this, Observer {
                //kiểm tra coi ngày đã chọn đã tồn tại trong database hay chưa
                if (it == null) {
                    //Ko tồn tại thì thêm
                    dateTime = DateTime(
                        calendar[Calendar.DAY_OF_MONTH],
                        calendar[Calendar.MONTH],
                        calendar[Calendar.YEAR]
                    )
                    dateTimeViewModel.insertDateTime(dateTime!!)
                } else {
                    //đã tồn tại thì check là thêm mới hay là chi tiết
                    if (isDetails) {
                        transaction?.let { transaction ->
                            transaction.idDate = it.id
                            transaction.note = edt_transaction_note.text.toString()
                            transaction.money =
                                handleTextToDouble(edt_transaction_money.text.toString()).toDouble()
                            transaction.month = calendar[Calendar.MONTH]
                            transaction.year = calendar[Calendar.YEAR]

                            transactionViewModel.updateTransaction(transaction)
                        }
                    } else {
                        transaction = Transaction(
                            handleTextToDouble(edt_transaction_money.text.toString()).toDouble(),
                            false,
                            it.id,
                            0,
                            edt_transaction_note.text.toString(),
                            calendar[Calendar.MONTH],
                            calendar[Calendar.YEAR]
                        )
                        transactionViewModel.insertTransaction(transaction!!)
                    }
                    finish()
                }
            })
    }

    private fun pickDateTime() {
        val dialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
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
                category = data?.getSerializableExtra(KEYS) as Category?
                Glide.with(this).load(category!!.iconUrl).into(img_categories)
                txt_title_transaction_category.text = category!!.title
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
