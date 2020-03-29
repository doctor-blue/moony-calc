package com.moony.calc.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moony.calc.R
import com.moony.calc.activities.AddTransactionActivity
import com.moony.calc.adapter.TransactionAdapter
import com.moony.calc.base.BaseFragment
import com.moony.calc.database.DateTimeViewModel
import com.moony.calc.model.DateTime
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

    //Dòng này dùng để lắng nghe nếu cái month thay đổi giá trị thì load dữ liệu tương ứng theo đó
    private var month: String by Delegates.observable(defaultTime) { _, oldValue, newValue ->

        //Xóa bỏ cái observe cũ đi nếu không nó sẽ chồng chéo lên nhau
        dateTimeViewModel.getAllDateTimeByMonth(oldValue).removeObservers(viewLifecycleOwner)

        //lấy data từ database
        dateTimeViewModel.getAllDateTimeByMonth(newValue)
            .observe(viewLifecycleOwner, Observer {
                createTransactionList(it)
            })
    }


    override fun getLayoutId(): Int = R.layout.fragment_transaction

    override fun init() {
        initControl()
        initEvent()
    }

    private fun initControl() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dateTimeViewModel =
            ViewModelProvider(fragmentActivity!!).get(DateTimeViewModel::class.java)

        month =
            "${calNow.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)} ${calNow.get(
                Calendar.YEAR
            )}"
        txt_transaction_date.text = month

    }

    private fun createTransactionList(list: List<DateTime>) {
        val adapter = TransactionAdapter(list, fragmentActivity!!) {
            //Nội dung của hàm itemClick ở đây

        }
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

    private fun showMonthYearPickerDialog(){
        val  builder =  MonthPickerDialog.Builder(fragmentActivity,
            MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->

                calNow.set(Calendar.YEAR,selectedYear)
                calNow.set(Calendar.MONTH,selectedMonth)

                month = "${calNow.getDisplayName(
                    Calendar.MONTH,
                    Calendar.LONG,
                    Locale.ENGLISH
                )} ${calNow.get(
                    Calendar.YEAR
                )}"
                txt_transaction_date.text = month
            },calNow.get(Calendar.YEAR),calNow.get(Calendar.MONTH))

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
