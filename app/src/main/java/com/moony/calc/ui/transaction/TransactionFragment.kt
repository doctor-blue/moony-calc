package com.moony.calc.ui.transaction

import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.devcomentry.moonlight.binding.BindingFragment
import com.moony.calc.R
import com.moony.calc.databinding.FragmentTransactionBinding
import com.moony.calc.model.TransactionItem
import com.moony.calc.utils.Settings
import com.moony.calc.utils.formatMonth
import com.whiteelephant.monthpicker.MonthPickerDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TransactionFragment :
    BindingFragment<FragmentTransactionBinding>(R.layout.fragment_transaction) {

    private var calNow = Calendar.getInstance()
    private val transactionViewModel: TransactionViewModel by activityViewModels()

    private var transactionAdapter: TransactionAdapter? = null
    private var transactionLiveData: LiveData<List<TransactionItem>>? = null

    private val settings: Settings by lazy {
        Settings.getInstance(requireContext())
    }
    private val currencyUnit by lazy {
        settings.getString(Settings.SettingKey.CURRENCY_UNIT)
    }


    override fun initControls(savedInstanceState: Bundle?) {
        refreshData()

        transactionAdapter = TransactionAdapter(transactionItemClick)

        binding {
            txtTransactionDate.text = calNow.formatMonth(Locale.ENGLISH)

            lifecycleOwner = viewLifecycleOwner

            currencyUnit = this@TransactionFragment.currencyUnit
            adapter = transactionAdapter
            vm = transactionViewModel
        }
    }

    private val transactionObserver = Observer<List<TransactionItem>> { list ->
        transactionAdapter!!.setAllTransaction(list)
        transactionViewModel.submitList(list)
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

    override fun initEvents() {
        binding {
            btnAddTransaction.setOnClickListener {
                findNavController().navigate(R.id.action_transaction_fragment_to_addTransactionFragment)
            }

            btnFilterTransaction.setOnClickListener {
                val popupMenu =
                    PopupMenu(requireActivity(), btnFilterTransaction)
                popupMenu.inflate(R.menu.transaction_filter_menu)
                popupMenu.setOnMenuItemClickListener { item ->
                    filterItemClicked(item)
                }
                popupMenu.show()
            }

            btnNextMonth.setOnClickListener {
                //add 1 month
                calNow.add(Calendar.MONTH, 1)
                txtTransactionDate.text = calNow.formatMonth(Locale.ENGLISH)

                transactionViewModel.setLoading(true)
                refreshData()
            }
            btnPreMonth.setOnClickListener {

                calNow.add(Calendar.MONTH, -1)
                txtTransactionDate.text = calNow.formatMonth(Locale.ENGLISH)

                transactionViewModel.setLoading(true)
                refreshData()
            }
            txtTransactionDate.setOnClickListener {
                showMonthYearPickerDialog()
            }
        }
    }

    private fun showMonthYearPickerDialog() {
        val builder = MonthPickerDialog.Builder(
            requireActivity(),
            { selectedMonth, selectedYear ->

                calNow.set(Calendar.YEAR, selectedYear)
                calNow.set(Calendar.MONTH, selectedMonth)

                binding.txtTransactionDate.text = calNow.formatMonth(Locale.ENGLISH)

                transactionViewModel.setLoading(true)
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

    private fun filterItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnu_filter_by_income -> {
                transactionAdapter!!.filter.filter("Income")
                Toast.makeText(requireActivity(), "By Income", Toast.LENGTH_SHORT).show()
            }

            R.id.mnu_filter_by_expenses -> {
                transactionAdapter!!.filter.filter("Expenses")
                Toast.makeText(requireActivity(), "By Expenses", Toast.LENGTH_SHORT).show()
            }

            R.id.mnu_filter_all -> {
                transactionAdapter!!.filter.filter("All")
                Toast.makeText(requireActivity(), "All", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

}
