package com.moony.calc.ui.transaction.adapter

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.devcomentry.moonlight.binding.BindingListAdapter
import com.moony.calc.R
import com.moony.calc.databinding.TransactionItemBinding
import com.moony.calc.model.TransactionItem

class TransactionAdapter(
    private val itemClick: (TransactionItem) -> Unit
) : Filterable,
    BindingListAdapter<TransactionItem, TransactionItemBinding>(
        R.layout.transaction_item
    ) {

    private var allTransactions: List<TransactionItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = getBinding(parent)
        return TransactionViewHolder(itemClick, binding)
    }

    fun setAllTransaction(transactions: List<TransactionItem>) {
        allTransactions = transactions
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val resultList: MutableList<TransactionItem> = mutableListOf()
                when (constraint) {
                    "Income" -> {
                        for (item in allTransactions) {
                            if (item.category.isIncome) {
                                resultList.add(item)
                            }
                        }
                        itemList = resultList
                    }
                    "Expenses" -> {
                        for (item in allTransactions) {
                            if (!item.category.isIncome) {
                                resultList.add(item)
                            }
                        }
                        itemList = resultList
                    }

                    "All" -> {
                        itemList = allTransactions
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = itemList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as List<TransactionItem>)
            }

        }
    }

}