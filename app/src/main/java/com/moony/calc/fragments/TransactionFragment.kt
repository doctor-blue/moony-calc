package com.moony.calc.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moony.calc.R
import com.moony.calc.activities.AddTransactionActivity
import com.moony.calc.adapter.TransactionAdapter
import com.moony.calc.base.BaseFragment
import com.moony.calc.database.DateTimeViewModel
import com.moony.calc.database.TransactionViewModel
import com.moony.calc.keys.MoonyKey
import com.moony.calc.model.Category
import com.moony.calc.model.DateTime
import com.moony.calc.model.Transaction
import com.whiteelephant.monthpicker.MonthPickerDialog
import kotlinx.android.synthetic.main.fragment_transaction.*
import java.util.*
import kotlin.properties.Delegates

class TransactionFragment : BaseFragment() {
    private var calNow = Calendar.getInstance()
    private lateinit var dateTimeViewModel: DateTimeViewModel
    private val defaultTime: String =
        "${calNow.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)} ${calNow.get(
            Calendar.YEAR
        )}"
    private lateinit var transactionViewModel: TransactionViewModel
    private var totalIncome=0.0
    private var totalExpenses=0.0
    private  var totalIncomeLiveData:LiveData<Double>?=null
    private var totalExpensesLiveData:LiveData<Double>?=null
    private var dateTimeLiveData:LiveData<List<DateTime>>?=null

    //Dòng này dùng để lắng nghe nếu cái month thay đổi giá trị thì load dữ liệu tương ứng theo đó
    private var month: String by Delegates.observable(defaultTime) { _, oldValue, newValue ->

        //Xóa bỏ cái observe cũ đi nếu không nó sẽ chồng chéo lên nhau
        dateTimeLiveData?.removeObserver(dateTimeObserver)

        //lấy data từ database
        dateTimeLiveData=dateTimeViewModel.getAllDateTimeByMonth(newValue)

        dateTimeLiveData!!.observe(viewLifecycleOwner,dateTimeObserver)


        totalIncomeLiveData?.removeObserver(totalIncomeObserver)
        totalIncomeLiveData=transactionViewModel.getTotalMoney(true, newValue)
        totalIncomeLiveData!!.observe(viewLifecycleOwner,totalIncomeObserver)

        totalExpensesLiveData?.removeObserver(totalExpensesObserver)
        totalExpensesLiveData=transactionViewModel.getTotalMoney(false,newValue)
    }


    override fun getLayoutId(): Int = R.layout.fragment_transaction

    override fun init() {
        initControl()
        initEvent()
    }

    private fun initControl() {

    }
    private val dateTimeObserver= Observer<List<DateTime>> {
        Log.d("TEST_LIST"," date time")
        createTransactionList(it)
    }
    private val totalIncomeObserver= Observer<Double> {income->
        totalIncome=0.0
        if (income!=null) totalIncome=income
        Log.d("INCOME"," inc $income")
        totalExpensesLiveData!!.removeObserver(totalExpensesObserver)
        totalExpensesLiveData!!.observe(viewLifecycleOwner,totalExpensesObserver)

    }
    private val totalExpensesObserver= Observer<Double> {expenses->
        totalExpenses=0.0
        if (expenses!=null) totalExpenses=expenses
        Log.d("INCOME"," exp  $expenses")
        txt_transaction_income.text = "$totalIncome"
        txt_transaction_expenses.text = "$totalExpenses"
        txt_transaction_balance.text = "${totalIncome - totalExpenses}"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dateTimeViewModel =
            ViewModelProvider(fragmentActivity!!).get(DateTimeViewModel::class.java)
        transactionViewModel =
            ViewModelProvider(fragmentActivity!!)[TransactionViewModel::class.java]

        month =
            "${calNow.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)} ${calNow.get(
                Calendar.YEAR
            )}"
        txt_transaction_date.text = month

    }

    private fun createTransactionList(list: List<DateTime>) {
        val adapter = TransactionAdapter(list, fragmentActivity!!, transactionItemClick)
        rv_transaction.setHasFixedSize(true)
        rv_transaction.layoutManager = LinearLayoutManager(fragmentActivity)
        rv_transaction.adapter = adapter

        if (list.isEmpty()) {
            layout_list_empty.visibility = View.VISIBLE
            rv_transaction.visibility = View.GONE
        } else {
            layout_list_empty.visibility = View.GONE
            rv_transaction.visibility = View.VISIBLE
        }

    }

    private val transactionItemClick: (Transaction, DateTime, Category) -> Unit =
        { transaction, dateTime, category ->
            //Nội dung của hàm itemClick ở đây
            val intent = Intent(context, AddTransactionActivity::class.java)
            intent.putExtra(MoonyKey.transactionDetail, transaction)
            intent.putExtra(MoonyKey.transactionCategory, category)
            intent.putExtra(MoonyKey.transactionDateTime, dateTime)
            startActivity(intent)
        }

    private fun initEvent() {
        btn_add_transaction.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    AddTransactionActivity::class.java
                )
            )
        }

        btn_next_month.setOnClickListener {
            //tiến thêm 1 tháng
            calNow.add(Calendar.MONTH, 1)
            month = "${calNow.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.ENGLISH
            )} ${calNow.get(
                Calendar.YEAR
            )}"
            txt_transaction_date.text = month
        }
        btn_pre_month.setOnClickListener {

            calNow.add(Calendar.MONTH, -1)
            month = "${calNow.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.ENGLISH
            )} ${calNow.get(
                Calendar.YEAR
            )}"
            txt_transaction_date.text = month
        }
        txt_transaction_date.setOnClickListener {
            showMonthYearPickerDialog()
        }

    }

    private fun showMonthYearPickerDialog() {
        val builder = MonthPickerDialog.Builder(fragmentActivity,
            MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->

                calNow.set(Calendar.YEAR, selectedYear)
                calNow.set(Calendar.MONTH, selectedMonth)

                month = "${calNow.getDisplayName(
                    Calendar.MONTH,
                    Calendar.LONG,
                    Locale.ENGLISH
                )} ${calNow.get(
                    Calendar.YEAR
                )}"
                txt_transaction_date.text = month
            }, calNow.get(Calendar.YEAR), calNow.get(Calendar.MONTH)
        )

        builder.setActivatedMonth(Calendar.JULY)
            .setMinYear(1990)
            .setActivatedYear(calNow.get(Calendar.YEAR))
            .setMaxYear(2100)
            .setActivatedMonth(calNow.get(Calendar.MONTH))
            .setMinMonth(Calendar.JANUARY)
            .setTitle(resources.getString(R.string.select_month))
            .setMaxMonth(Calendar.DECEMBER)
            .build()
            .show()

    }


}
