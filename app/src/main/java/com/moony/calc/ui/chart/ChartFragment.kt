package com.moony.calc.ui.chart

import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.faskn.lib.PieChart
import com.faskn.lib.Slice
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentChartBinding
import com.moony.calc.model.ChartItem
import com.moony.calc.ui.transaction.TransactionViewModel
import com.moony.calc.utils.formatMonth
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.util.*

class ChartFragment : BaseFragment() {

    private val binding: FragmentChartBinding
        get() = (getViewBinding() as FragmentChartBinding)

    private var slices = arrayListOf<Slice>()
    private var sumOfGradedItems: Double = 0.0
    private var gradedItems: List<ChartItem> = listOf()
    private var isIncome: Boolean = false
    private var calNow = Calendar.getInstance()
    private var chartAdapter: ChartAdapter? = null
    private var chartLegendAdapter: ChartLegendAdapter? = null
    private val sliceWidth by lazy {
        requireContext().resources.getDimension(R.dimen._16sdp)
    }
    private val sliceStartPoint = 0f

    @ColorRes
    val colors = arrayListOf<Int>(
        R.color.chart_color_1,
        R.color.chart_color_2,
        R.color.chart_color_3,
        R.color.chart_color_4,
        R.color.chart_color_5
    )

    private val transactionViewModel: TransactionViewModel by lazy {
        ViewModelProvider(fragmentActivity!!)[TransactionViewModel::class.java]
    }

    override fun getLayoutId(): Int = R.layout.fragment_chart

    override fun initControls(view: View, savedInstanceState: Bundle?) {
        generateData()
        binding.txtTransactionDate.text = calNow.formatMonth(Locale.ENGLISH)
        chartAdapter = ChartAdapter(fragmentActivity!!, chartItemClick)
        binding.rvChart.setHasFixedSize(true)
        binding.rvChart.layoutManager = LinearLayoutManager(fragmentActivity)
        binding.rvChart.adapter = chartAdapter
        chartLegendAdapter = ChartLegendAdapter(fragmentActivity!!)
        binding.rvChartLegend.setHasFixedSize(true)
        binding.rvChartLegend.layoutManager = LinearLayoutManager(fragmentActivity)
        binding.rvChartLegend.adapter = chartLegendAdapter
    }

    override fun initEvents() {
        binding.centerText.setOnClickListener {
            isIncome = !isIncome
            generateData()
        }

        binding.btnNextMonth.setOnClickListener {
//            tiến thêm 1 tháng
            calNow.add(Calendar.MONTH, 1)
            binding.txtTransactionDate.text = calNow.formatMonth(Locale.ENGLISH)

//            showProgressBar()
            generateData()
        }
        binding.btnPreMonth.setOnClickListener {

            calNow.add(Calendar.MONTH, -1)
            binding.txtTransactionDate.text = calNow.formatMonth(Locale.ENGLISH)

//            showProgressBar()
            generateData()
        }
        binding.txtTransactionDate.setOnClickListener {
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

        if (slices.isNotEmpty()) {
            val pieChart = PieChart(
                slices = slices,
                clickListener = null,
                sliceStartPoint = sliceStartPoint,
                sliceWidth = sliceWidth
            ).build()
            binding.chart.setPieChart(pieChart)
            chartLegendAdapter!!.refreshLegend(slices)
            binding.rvChartLegend.visibility = View.VISIBLE
        } else {
            binding.chart.visibility = View.GONE
            binding.chart.visibility = View.VISIBLE
            binding.rvChartLegend.visibility = View.GONE
        }

        binding.centerText.text = changeCenterText(isIncome, sumOfGradedItems)

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

                binding.txtTransactionDate.text = calNow.formatMonth(Locale.ENGLISH)

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