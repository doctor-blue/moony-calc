package com.moony.calc.ui.transaction

import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.model.TransactionItem
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.formatMonth
import com.whiteelephant.monthpicker.MonthPickerDialog
import kotlinx.android.synthetic.main.fragment_transaction.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class TransactionFragment : BaseFragment() {
    private var calNow = Calendar.getInstance()
    private val transactionViewModel: TransactionViewModel by lazy {
        ViewModelProvider(fragmentActivity!!)[TransactionViewModel::class.java]
    }
    private var totalIncome: Double = 0.0
    private var totalExpenses: Double = 0.0
    private var transactionAdapter: TransactionAdapter? = null
    private var transactionLiveData: LiveData<List<TransactionItem>>? = null

    private val settings: Settings by lazy {
        Settings.getInstance(baseContext!!)
    }


    override fun getLayoutId(): Int = R.layout.fragment_transaction

    override fun init() {
        initControl()
        initEvent()
    }

    private fun initControl() {
        refreshData()
        txt_transaction_date.text = calNow.formatMonth(Locale.ENGLISH)
        transactionAdapter = TransactionAdapter(fragmentActivity!!, transactionItemClick)
        rv_transaction.setHasFixedSize(true)
        rv_transaction.layoutManager = LinearLayoutManager(fragmentActivity)
        rv_transaction.adapter = transactionAdapter

    }

    private val transactionObserver = Observer<List<TransactionItem>> { list ->
        transactionAdapter!!.refreshTransactions(list)
        totalExpenses = 0.0
        totalIncome = 0.0

        lifecycleScope.launch {
            list.asFlow().collect {
                if (it.category.isIncome) {
                    totalIncome += it.transaction.money
                } else {
                    totalExpenses += it.transaction.money
                }
                updateTotalTransaction()
            }
        }

        if (list.isEmpty()){
            updateTotalTransaction()
        }

        progress_loading.visibility = View.INVISIBLE

        if (list.isEmpty()) {
            layout_list_empty.visibility = View.VISIBLE
            rv_transaction.visibility = View.GONE
        } else {
            layout_list_empty.visibility = View.GONE
            rv_transaction.visibility = View.VISIBLE
        }
    }

    private fun updateTotalTransaction() {

        txt_transaction_income.text =
            (totalIncome.decimalFormat() + settings.getString(Settings.SettingKey.CURRENCY_UNIT))
        txt_transaction_expenses.text =
            (totalExpenses.decimalFormat() + settings.getString(Settings.SettingKey.CURRENCY_UNIT))
        txt_transaction_balance.text =
            ((totalIncome - totalExpenses).decimalFormat() + settings.getString(Settings.SettingKey.CURRENCY_UNIT))

    }


    private fun refreshData() {
        transactionLiveData?.removeObserver(transactionObserver)
        transactionLiveData = transactionViewModel.getAllTransactionItem(
            calNow[Calendar.MONTH],
            calNow[Calendar.YEAR]
        )
        transactionLiveData!!.observe(viewLifecycleOwner, transactionObserver)



    }

    private val transactionItemClick: (TransactionItem) -> Unit =
        { transactionItem ->

            val bundle = bundleOf(
                getString(R.string.transaction) to transactionItem,
            )
            findNavController().navigate(
                R.id.action_transaction_fragment_to_transactionDetailFragment,
                bundle
            )
        }

    private fun initEvent() {
        btn_add_transaction.setOnClickListener {
            findNavController().navigate(R.id.action_transaction_fragment_to_addTransactionFragment)
        }

        btn_next_month.setOnClickListener {
            //tiến thêm 1 tháng
            calNow.add(Calendar.MONTH, 1)
            txt_transaction_date.text = calNow.formatMonth(Locale.ENGLISH)

            showProgressBar()
            refreshData()
        }
        btn_pre_month.setOnClickListener {

            calNow.add(Calendar.MONTH, -1)
            txt_transaction_date.text = calNow.formatMonth(Locale.ENGLISH)

            showProgressBar()
            refreshData()
        }
        txt_transaction_date.setOnClickListener {
            showMonthYearPickerDialog()
        }
    }

    private fun showProgressBar() {
        progress_loading.visibility = View.VISIBLE
        layout_list_empty.visibility = View.GONE
        rv_transaction.visibility = View.GONE
    }

    private fun showMonthYearPickerDialog() {
        val builder = MonthPickerDialog.Builder(
            fragmentActivity,
            { selectedMonth, selectedYear ->

                calNow.set(Calendar.YEAR, selectedYear)
                calNow.set(Calendar.MONTH, selectedMonth)

                txt_transaction_date.text = calNow.formatMonth(Locale.ENGLISH)

                showProgressBar()
                refreshData()
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
