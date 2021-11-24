package com.moony.calc.ui.category.adapter

import android.view.ViewGroup
import com.devcomentry.moonlight.binding.BindingListAdapter
import com.moony.calc.R
import com.moony.calc.databinding.CategoryItemBinding
import com.moony.calc.ui.category.adapter.CategoryIconViewHolder
import com.moony.calc.ui.category.adapter.CategoryViewHolder
import com.moony.calc.ui.category.adapter.CategoryViewHolderBase
import com.moony.calc.ui.category.CategoryViewModel
import com.moony.calc.ui.transaction.TransactionViewModel

class CategoryAdapter(
    private val categoryViewModel: CategoryViewModel? = null,
    private val transactionViewModel: TransactionViewModel? = null,
    private val canRemove: Boolean = false,
    private val onClick: (Any) -> Unit

) : BindingListAdapter<Any, CategoryItemBinding>(R.layout.category_item) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolderBase {
        val binding = getBinding(parent)
        return if (categoryViewModel != null) {
            CategoryViewHolder(
                categoryViewModel,
                transactionViewModel!!,
                canRemove,
                binding,
                onClick
            )
        } else {
            CategoryIconViewHolder(binding, onClick)
        }
    }

}