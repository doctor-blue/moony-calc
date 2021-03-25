package com.moony.calc.ui.chart

import android.opengl.Visibility
import android.util.Log
import android.view.View
import androidx.annotation.ColorRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.faskn.lib.PieChart
import com.faskn.lib.Slice
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.model.ChartItem
import com.moony.calc.ui.transaction.TransactionAdapter
import com.moony.calc.ui.transaction.TransactionViewModel
import com.moony.calc.utils.formatMonth
import com.whiteelephant.monthpicker.MonthPickerDialog
import kotlinx.android.synthetic.main.fragment_chart.*
import kotlinx.android.synthetic.main.fragment_chart.btn_next_month
import kotlinx.android.synthetic.main.fragment_chart.btn_pre_month
import kotlinx.android.synthetic.main.fragment_chart.txt_transaction_date
import kotlinx.android.synthetic.main.fragment_transaction.*
import java.util.*
import kotlin.collections.ArrayList

class ChartFragment : BaseFragment() {
    private var slices = arrayListOf<Slice>()
    private var sumOfGradedItems: Double = 0.0
    private var gradedItems: List<ChartItem> = listOf()
    private var isIncome: Boolean = false
    private var calNow = Calendar.getInstance()
    private var chartAdapter: ChartAdapter? = null
    private var chartLegendAdapter: ChartLegendAdapter? = null

    companion object {
        const val SLICE_WIDTH = 80f
        const val SLICE_START_POINT = 0f
    }

    @ColorRes
    val colors = arrayListOf<Int>(
        R.color.colorAccent,
        R.color.blue,
        R.color.colorPrimary,
        R.color.backgroundColor,
        R.color.brown_tint
    )

    private val transactionViewModel: TransactionViewModel by lazy {
        ViewModelProvider(fragmentActivity!!)[TransactionViewModel::class.java]
    }

    override fun init() {
        initControl()
        initEvent()
    }

    override fun getLayoutId(): Int = R.layout.fragment_chart

    private fun initControl() {
        generateData()
        chartAdapter = ChartAdapter(fragmentActivity!!, chartItemClick)
        rv_chart.setHasFixedSize(true)
        rv_chart.layoutManager = LinearLayoutManager(fragmentActivity)
        rv_chart.adapter = chartAdapter
        chartLegendAdapter = ChartLegendAdapter(fragmentActivity!!)
        rv_chart_legend.setHasFixedSize(true)
        rv_chart_legend.layoutManager = LinearLayoutManager(fragmentActivity)
        rv_chart_legend.adapter = chartLegendAdapter
    }

    private fun initEvent() {
        center_text.setOnClickListener {
            isIncome = !isIncome
            generateData()
        }

        btn_next_month.setOnClickListener {
//            tiến thêm 1 tháng
            calNow.add(Calendar.MONTH, 1)
            txt_transaction_date.text = calNow.formatMonth(Locale.ENGLISH)

//            showProgressBar()
            generateData()
        }
        btn_pre_month.setOnClickListener {

            calNow.add(Calendar.MONTH, -1)
            txt_transaction_date.text = calNow.formatMonth(Locale.ENGLISH)

//            showProgressBar()
            generateData()
        }
        txt_transaction_date.setOnClickListener {
            showMonthYearPickerDialog()
        }
    }

    private fun generateData() {
        transactionViewModel.getChartData(calNow[Calendar.MONTH], calNow[Calendar.YEAR])
            .observe(viewLifecycleOwner, Observer { items ->
                gradedItems = items.filter { it.category.isIncome == isIncome }
                    .sortedByDescending { it.sum }
                chartAdapter!!.refreshChartItems(gradedItems)
                drawChart(gradedItems)
            })
    }

    private val chartItemClick: (ChartItem) -> Unit = {

    }

    private fun drawChart(gradedItems: List<ChartItem>) {
        slices.clear()
        sumOfGradedItems = 0.0
        for ((i, item) in gradedItems.withIndex()) {
            slices.add(
                Slice(
                    item.sum.toFloat(),
                    if (i < 5) colors[i] else colors[0],
                    item.category.title
                )
            )
            sumOfGradedItems += item.sum
        }

        if (slices.size > 5) {
            slices = slices.take(4) as ArrayList<Slice>
            var sumOfFourItems = 0.0f
            slices.forEach {
                sumOfFourItems += it.dataPoint
            }
            slices.add(Slice(sumOfGradedItems.toFloat() - sumOfFourItems, colors[4], "Other"))
        }

        if(slices.isNotEmpty()) {
            val pieChart = PieChart(
                slices = slices,
                clickListener = null,
                sliceStartPoint = SLICE_START_POINT,
                sliceWidth = SLICE_WIDTH
            ).build()
            chart.setPieChart(pieChart)
            chartLegendAdapter!!.refreshLegend(slices)
            rv_chart_legend.visibility = View.VISIBLE
        } else {
            chart.visibility = View.GONE
            chart.visibility = View.VISIBLE
            rv_chart_legend.visibility = View.GONE
        }

        center_text.text = changeCenterText(isIncome, sumOfGradedItems)

    }

    private fun changeCenterText(isIncome: Boolean, sum: Double): String =
        if (isIncome) "${resources.getString(R.string.income)}\n$sum\$" else "${
            resources.getString(
                R.string.expenses
            )
        }\n$sum\$"

    private fun showMonthYearPickerDialog() {
        val builder = MonthPickerDialog.Builder(
            fragmentActivity,
            { selectedMonth, selectedYear ->

                calNow.set(Calendar.YEAR, selectedYear)
                calNow.set(Calendar.MONTH, selectedMonth)

                txt_transaction_date.text = calNow.formatMonth(Locale.ENGLISH)

//                showProgressBar()
                generateData()
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