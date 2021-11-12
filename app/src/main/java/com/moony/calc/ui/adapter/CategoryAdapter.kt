package com.moony.calc.ui.adapter

import android.view.ViewGroup
import com.devcomentry.moonlight.binding.BindingListAdapter
import com.moony.calc.R
import com.moony.calc.databinding.CategoryItemBinding
import com.moony.calc.ui.adapter.viewholder.CategoryIconViewHolder
import com.moony.calc.ui.adapter.viewholder.CategoryViewHolder
import com.moony.calc.ui.adapter.viewholder.CategoryViewHolderBase
import com.moony.calc.ui.category.CategoryViewModel
import com.moony.calc.ui.transaction.TransactionViewModel

class CategoryAdapter(
    private val categoryViewModel: CategoryViewModel? = null,
    private val transactionViewModel: TransactionViewModel? = null,
    private val canRemove: Boolean = false,
    private val onClick: (Any) -> Unit

) : BindingListAdapter<Any, CategoryItemBinding>(R.layout.category_item) {

//    private var links: List<String>? = null
//    private var categories: List<Category> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolderBase {
        val binding = getBinding(parent)
      return  if (categoryViewModel != null){
            CategoryViewHolder(categoryViewModel,transactionViewModel!!,canRemove,binding,onClick)
        }else{
            CategoryIconViewHolder(binding,onClick)
      }
    }

//    override fun getItemCount(): Int = links?.size ?: categories.size
//
//    override fun onBindViewHolder(holder: BindingViewHolder<Any,CategoryItemBinding>, position: Int) {
//        if (links != null) {
//            holder.onBind(links!![position])
//        } else {
//            holder.onBind(categories[position])
//        }
//    }
//
//    fun setCategoryList(categories: List<Category>) {
//        this.categories = categories
//        notifyDataSetChanged()
//    }
//
//    fun setLinks(links: List<String>?) {
//        this.links = links
//        notifyDataSetChanged()
//    }
}