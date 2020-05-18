package com.moony.calc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moony.calc.R
import com.moony.calc.database.DateTimeViewModel
import com.moony.calc.database.TransactionViewModel
import com.moony.calc.model.Category
import com.moony.calc.model.DateTime
import com.moony.calc.model.Transaction
import com.moony.calc.utils.decimalFormat
import kotlin.math.roundToLong

class TransactionAdapter(
    private val dates: List<DateTime>,
    private val activity: FragmentActivity,
    private val itemClick: (Transaction, DateTime, Category) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.transaction_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dates.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(dates[position], itemClick)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTransactionDay: TextView = itemView.findViewById(R.id.txt_transaction_day)
        private val txtIncome: TextView = itemView.findViewById(R.id.txt_transaction_income)
        private val txtExpenses: TextView = itemView.findViewById(R.id.txt_transaction_expenses)
        private val rvTransaction: RecyclerView = itemView.findViewById(R.id.rv_transaction_item)

        fun onBind(dateTime: DateTime, itemClick: (Transaction, DateTime, Category) -> Unit) {
            txtTransactionDay.text = dateTime.day.toString()

            val transactionViewModel =
                ViewModelProvider(activity).get(TransactionViewModel::class.java)
            val dateTimeViewModel = ViewModelProvider(activity)[DateTimeViewModel::class.java]
            //Lấy Transaction theo từng ngày trong tháng và gộp nó vào 1 list để hiển thị trong list con
            transactionViewModel.getAllTransactionByDate(dateTime.id).observe(activity, Observer {

                var income = 0.0
                var expenses = 0.0

                for (transaction in it)
                    if (transaction.isIncome) income += transaction.money else expenses += transaction.money

                income = ((income * 1000.0).roundToLong() / 1000.0)
                expenses = ((expenses * 1000.0).roundToLong() / 1000.0)

                if (it.isEmpty())
                    dateTimeViewModel.deleteDateTime(dateTime)


                txtExpenses.text = expenses.decimalFormat()
                txtIncome.text = income.decimalFormat()
                //itemClick ở đây là 1 hàm mà nội dung của hàm sẽ được viết ở TransactionFragment
                val adapter = TransactionChildrenAdapter(it, itemClick, activity, dateTime)
                val layoutManager = LinearLayoutManager(activity)
                rvTransaction.setHasFixedSize(true)
                rvTransaction.layoutManager = layoutManager
                rvTransaction.adapter = adapter

            })
        }
    }
}