package com.moony.calc.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.moony.calc.R
import com.moony.calc.model.Category
import com.moony.calc.model.Transaction
import com.moony.calc.model.TransactionItem
import com.moony.calc.ui.category.CategoryViewModel
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat

class TransactionAdapter(
    private val context: FragmentActivity,
    private val itemClick: (TransactionItem) -> Unit
) : Filterable, RecyclerView.Adapter<TransactionViewHolder>() {
    private var transactions: List<TransactionItem> = listOf()
    private var allTransactions: List<TransactionItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(view,itemClick)
    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.onBind(transactions[position])
    }

    fun refreshTransactions(transactions: List<TransactionItem>) {
        this.transactions = transactions
        allTransactions = transactions
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val resultList: MutableList<TransactionItem> = mutableListOf()
                when(constraint) {
                    "Income" -> {
                        for(item in allTransactions) {
                            if(item.category.isIncome) {
                                resultList.add(item)
                            }
                        }
                        transactions = resultList
                    }
                    "Expenses" -> {
                        for(item in allTransactions) {
                            if(!item.category.isIncome) {
                                resultList.add(item)
                            }
                        }
                        transactions = resultList
                    }

                    "All" -> {
                        transactions = allTransactions
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = transactions
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                transactions = results?.values as List<TransactionItem>
                notifyDataSetChanged()
            }

        }
    }


}