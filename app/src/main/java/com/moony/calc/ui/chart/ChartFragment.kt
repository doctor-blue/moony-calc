package com.moony.calc.ui.chart

import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devcomentry.moonlight.binding.BindingFragment
import com.faskn.lib.PieChart
import com.faskn.lib.Slice
import com.moony.calc.R
import com.moony.calc.databinding.FragmentChartBinding
import com.moony.calc.model.ChartItem
import com.moony.calc.ui.transaction.TransactionViewModel
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.formatMonth
import com.whiteelephant.monthpicker.MonthPickerDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChartFragment : BindingFragment<FragmentChartBinding>(R.layout.fragment_chart) {

    private var slices = arrayListOf<Slice>()
    private var sumOfGradedItems: Double = 0.0
    private var gradedItems: List<ChartItem> = listOf()
    private var isIncome: Boolean = false
    private var calNow = Calendar.getInstance()

    private val chartAdapter: ChartAdapter by lazy { ChartAdapter(chartItemClick) }

    private val chartLegendAdapter: ChartLegendAdapter = ChartLegendAdapter()
    private val sliceWidth by lazy {
        requireContext().resources.getDimension(R.dimen._16sdp)
    }
    private val settings: Settings by lazy {
        Settings.getInstance(requireContext())
    }
    private val sliceStartPoint = 0f

    @ColorRes
    val colors = arrayListOf(
            R.color.chart_color_1,
            R.color.chart_color_2,
            R.color.chart_color_3,
            R.color.chart_color_4,
            R.color.chart_color_5
    )

    private val transactionViewModel: TransactionViewModel by activityViewModels()


    override fun initControls(savedInstanceState: Bundle?) {
        generateData()
        binding.txtTransactionDate.text = calNow.formatMonth(Locale.ENGLISH)
        binding.rvChart.setHasFixedSize(true)
        binding.rvChart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChart.adapter = chartAdapter


        binding.rvChartLegend.setHasFixedSize(true)
        binding.rvChartLegend.layoutManager = LinearLayoutManager(requireContext())
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
                .observe(viewLifecycleOwner, { items ->
                    gradedItems = items.filter { it.category.isIncome == isIncome }
                            .sortedByDescending { it.sum }
                    chartAdapter.submitList(gradedItems)
                    drawChart(gradedItems)
                })
    }

    private val chartItemClick: (ChartItem) -> Unit = {

    }

    private fun drawChart(gradedItems: List<ChartItem>) {
        slices.clear()
        if (gradedItems.isEmpty()) {
            binding.layoutListEmpty.visibility = View.VISIBLE
            binding.rvChart.visibility = View.GONE
            binding.nullLegendLayout.visibility = View.VISIBLE
        } else {
            binding.layoutListEmpty.visibility = View.GONE
            binding.rvChart.visibility = View.VISIBLE
            binding.nullLegendLayout.visibility = View.GONE
        }
        sumOfGradedItems = 0.0
        for ((i, item) in gradedItems.withIndex()) {
            slices.add(
                    Slice(
                            item.sum.toFloat(),
                            if (i < 5) colors[i] else colors[0],
                            if (item.category.resId != -1) resources.getString(item.category.resId) else
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
            chartLegendAdapter.submitList(slices)
            binding.rvChartLegend.visibility = View.VISIBLE
        } else {
            binding.chart.visibility = View.GONE
            binding.chart.visibility = View.VISIBLE
            binding.rvChartLegend.visibility = View.GONE
        }

        binding.centerText.text = changeCenterText(isIncome, sumOfGradedItems)

    }

    private fun changeCenterText(isIncome: Boolean, sum: Double): String =
            if (isIncome) "${resources.getString(R.string.income)}\n${sum.decimalFormat()}${settings.getString(Settings.SettingKey.CURRENCY_UNIT)}" else "${
                resources.getString(
                        R.string.expenses
                )
            }\n${sum.decimalFormat()}${settings.getString(Settings.SettingKey.CURRENCY_UNIT)}"

    private fun showMonthYearPickerDialog() {
        val builder = MonthPickerDialog.Builder(
                requireContext(),
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