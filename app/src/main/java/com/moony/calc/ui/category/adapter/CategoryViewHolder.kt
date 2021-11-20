package com.moony.calc.ui.category.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.devcomentry.moonlight.binding.BindingViewHolder
import com.moony.calc.R
import com.moony.calc.databinding.CategoryItemBinding
import com.moony.calc.model.Category
import com.moony.calc.ui.category.CategoryViewModel
import com.moony.calc.ui.transaction.TransactionViewModel
import com.moony.calc.utils.showDialogDelete

abstract class CategoryViewHolderBase(
    protected val binding: CategoryItemBinding
) : BindingViewHolder<Any, CategoryItemBinding>(binding)

class CategoryViewHolder(
    private val categoryViewModel: CategoryViewModel,
    private val transactionViewModel: TransactionViewModel,
    private val canRemove: Boolean,
    binding: CategoryItemBinding,
    private val onClick: (Any) -> Unit
) : CategoryViewHolderBase(binding) {

    private var category: Category? = null

    init {
        binding.imgCategoryItem.setOnClickListener {
            onClick(category!!)
        }
        binding.btnRemoveCategory.setOnClickListener {
            itemView.context.showDialogDelete(
                R.string.notice_delete_category
            ) {
                transactionViewModel.deleteAllTransactionByCategory(category!!.idCategory)
                categoryViewModel.deleteCategory(category!!)
            }


        }
    }


    override fun onBind(item: Any) {

        category = item as Category
        val categoryTitle =
            if (category!!.resId != -1) binding.txtCategoryItemName.context.resources.getString(
                category!!.resId
            ) else category!!.title

        if (category!!.title.length >= 10) {
            binding.txtCategoryItemName.text = (categoryTitle.substring(0, 10) + "...")
        } else {
            binding.txtCategoryItemName.text = categoryTitle
        }

        binding.btnRemoveCategory.visibility = View.GONE

        if (category!!.resId == R.string.add) {
            Glide.with(itemView.context).load(R.drawable.ic_add).into(binding.imgCategoryItem)
        } else {
            Glide.with(itemView.context).load("//android_asset/${category!!.iconUrl}")
                .into(binding.imgCategoryItem)
            if (canRemove && category!!.resId == -1) {
                binding.btnRemoveCategory.visibility = View.VISIBLE
            }
        }
    }
}