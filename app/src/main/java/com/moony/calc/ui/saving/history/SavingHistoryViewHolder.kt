package com.moony.calc.ui.saving.history

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.model.SavingHistoryItem
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.decimalFormat

class SavingHistoryViewHolder(itemView: View,itemClick: (SavingHistoryItem) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val imgCategory: ImageView = itemView.findViewById(R.id.img_saving_history_category)
    private val txtTitle: TextView =
        itemView.findViewById(R.id.txt_saving_history_title)
    private val txtMoney: TextView = itemView.findViewById(R.id.txt_saving_history_money)
    private val layoutContent =
        itemView.findViewById<LinearLayout>(R.id.layout_saving_history_item)

    private var savingHistoryItem :SavingHistoryItem? = null

    init {
        layoutContent.setOnClickListener {
            savingHistoryItem?.let {
                itemClick(it)
            }
        }
    }

    fun onBind(item: SavingHistoryItem) {
        savingHistoryItem = item

        Glide.with(itemView.context).load(AssetFolderManager.assetPath + item.category.iconUrl)
            .into(imgCategory)

        txtMoney.text = item.history.amount.decimalFormat()

        if (item.history.description.length > 10) {
            txtTitle.text = (item.history.description.substring(0, 10) + "...")
        } else {
            txtTitle.text = item.history.description
        }

    }
}