package com.moony.calc.ui.adapter.viewholder

import android.view.View
import com.devcomentry.moonlight.binding.BindingViewHolder
import com.moony.calc.R
import com.moony.calc.databinding.SavingHistoryItemBinding
import com.moony.calc.model.SavingHistory
import com.moony.calc.utils.decimalFormat

class SavingHistoryViewHolder(
        private val itemClick: (SavingHistory) -> Unit,
        private val binding: SavingHistoryItemBinding,
) : BindingViewHolder<SavingHistory, SavingHistoryItemBinding>(binding) {
    private var savingHistoryItem: SavingHistory? = null

    init {
        binding.layoutSavingHistoryItem.setOnClickListener {
            savingHistoryItem?.let {
                itemClick(it)
            }
        }
    }

  override  fun onBind(item: SavingHistory) {
        savingHistoryItem = item

        binding.txtSavingHistoryMoney.text = item.amount.decimalFormat()
        binding.txtSavingHistoryDate.text = item.date


        if (item.isSaving) {
            binding.txtSavingHistoryTitle.text = binding.root.resources.getString(R.string.money_in)
            binding.imgSavingHistory.setImageResource(R.drawable.ic_money_in)
        } else {
            binding.txtSavingHistoryTitle.text = binding.root.context.resources.getString(R.string.money_out)
            binding.imgSavingHistory.setImageResource(R.drawable.ic_money_out)
        }
    }
}