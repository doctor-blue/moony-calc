package com.moony.calc.ui.chart

import android.util.Log
import androidx.annotation.ColorRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.faskn.lib.PieChart
import com.faskn.lib.Slice
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.model.ChartItem
import com.moony.calc.ui.transaction.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_chart.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChartFragment : BaseFragment() {

    var slices = arrayListOf<Slice>()
    var sum: Double = 0.0

    @ColorRes val colors = arrayListOf<Int>(R.color.colorAccent, R.color.blue, R.color.colorPrimary, R.color.backgroundColor, R.color.brown_tint)

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
    }

    private fun initEvent() {

    }

    private fun generateData() {
        transactionViewModel.getChartData(2, 2021).observe(viewLifecycleOwner, Observer {
            for((i, item) in it.withIndex()) {
                slices.add(Slice(item.sum.toFloat(), colors[i], item.category.title))
                Log.d("DATA", i.toString())
            }

            val pieChart = PieChart(
                slices = slices, clickListener = null, sliceStartPoint = 0f, sliceWidth = 80f
            ).build()

            chart.setPieChart(pieChart)

        })
    }
}