package com.moony.calc.ui.adapter

import android.view.ViewGroup
import com.devcomentry.moonlight.binding.BindingListAdapter
import com.moony.calc.R
import com.moony.calc.databinding.SavingHistoryItemBinding
import com.moony.calc.model.SavingHistory
import com.moony.calc.ui.adapter.viewholder.SavingHistoryViewHolder

class SavingHistoryAdapter(
    private val itemClick: (SavingHistory) -> Unit
) : BindingListAdapter<SavingHistory,SavingHistoryItemBinding>(R.layout.saving_history_item) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingHistoryViewHolder {
        return SavingHistoryViewHolder(itemClick,getBinding(parent))
    }

}