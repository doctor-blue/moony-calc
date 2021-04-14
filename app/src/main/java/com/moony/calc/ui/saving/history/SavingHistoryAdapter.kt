package com.moony.calc.ui.saving.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moony.calc.R
import com.moony.calc.model.SavingHistory

class SavingHistoryAdapter(
    private val itemClick: (SavingHistory) -> Unit
) : RecyclerView.Adapter<SavingHistoryViewHolder>() {

    private var histories: List<SavingHistory> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingHistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.saving_history_item, parent, false)
        return SavingHistoryViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int = histories.size

    override fun onBindViewHolder(holder: SavingHistoryViewHolder, position: Int) {
        holder.onBind(histories[position])
    }

    fun refreshData(histories: List<SavingHistory>) {
        this.histories = histories
        notifyDataSetChanged()
    }

}