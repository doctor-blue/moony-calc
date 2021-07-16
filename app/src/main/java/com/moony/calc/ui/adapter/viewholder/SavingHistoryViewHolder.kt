package com.moony.calc.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.moony.calc.R
import com.moony.calc.model.SavingHistory
import com.moony.calc.utils.decimalFormat

class SavingHistoryViewHolder(itemView: View,itemClick: (SavingHistory) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val txtTitle: TextView =
        itemView.findViewById(R.id.txt_saving_history_title)
    private val txtMoney: TextView = itemView.findViewById(R.id.txt_saving_history_money)
    private val txtDate: TextView = itemView.findViewById(R.id.txt_saving_history_date)
    private val layoutContent =
        itemView.findViewById<CardView>(R.id.layout_saving_history_item)
    private val imgSavingHistory = itemView.findViewById<ImageView>(R.id.img_saving_history)

    private var savingHistoryItem :SavingHistory? = null

    init {
        layoutContent.setOnClickListener {
            savingHistoryItem?.let {
                itemClick(it)
            }
        }
    }

    fun onBind(item: SavingHistory) {
        savingHistoryItem = item

        txtMoney.text = item.amount.decimalFormat()
        txtDate.text = item.date

        if (item.isSaving) {
            txtTitle.text = txtTitle.context.resources.getString(R.string.money_in)
            imgSavingHistory.setImageResource(R.drawable.ic_money_in)
        } else {
            txtTitle.text = txtTitle.context.resources.getString(R.string.money_out)
            imgSavingHistory.setImageResource(R.drawable.ic_money_out)
        }
    }
}