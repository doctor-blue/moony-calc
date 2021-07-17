package com.moony.calc.ui.adapter.viewholder

import com.bumptech.glide.Glide
import com.moony.calc.databinding.CategoryItemBinding

class CategoryIconViewHolder(
    binding: CategoryItemBinding,
    private val onClick: (Any) -> Unit
) : CategoryViewHolderBase(binding) {

    private var link: String? = null

    init {
        binding.imgCategoryItem.setOnClickListener {
            onClick(link!!)
        }
    }

    override fun onBind(item: Any) {
        link = item as String
        Glide.with(itemView.context).load("//android_asset/$link").into(binding.imgCategoryItem)
        binding.txtCategoryItemName.text = ""
    }
}