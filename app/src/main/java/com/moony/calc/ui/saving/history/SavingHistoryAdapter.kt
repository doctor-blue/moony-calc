package com.moony.calc.ui.saving.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.ui.category.CategoryViewModel
import com.moony.calc.model.SavingHistory
import com.moony.calc.model.SavingHistoryItem
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.decimalFormat

class SavingHistoryAdapter(
    private val itemClick: (SavingHistoryItem) -> Unit
) : RecyclerView.Adapter<SavingHistoryAdapter.SavingHistoryViewHolder>() {

    private var histories: List<SavingHistoryItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingHistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.saving_history_item, parent, false)
        return SavingHistoryViewHolder(view)
    }

    override fun getItemCount(): Int = histories.size

    override fun onBindViewHolder(holder: SavingHistoryViewHolder, position: Int) {
        holder.onBind(histories[position])
    }

    fun refreshData(histories: List<SavingHistoryItem>){
        this.histories = histories
        notifyDataSetChanged()
    }


    inner class SavingHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgCategory: ImageView = itemView.findViewById(R.id.img_saving_history_category)
        private val txtTitle: TextView =
            itemView.findViewById(R.id.txt_saving_history_title)
        private val txtMoney: TextView = itemView.findViewById(R.id.txt_saving_history_money)
        private val layoutContent =
            itemView.findViewById<LinearLayout>(R.id.layout_saving_history_item)

        fun onBind(item: SavingHistoryItem) {
            Glide.with(itemView.context).load(AssetFolderManager.assetPath + item.category.iconUrl)
                .into(imgCategory)

            txtMoney.text = item.history.amount.decimalFormat()

            if (item.history.description.length > 10) {
                txtTitle.text = (item.history.description.substring(0, 10) + "...")
            } else {
                txtTitle.text = item.history.description
            }
            layoutContent.setOnClickListener {
                itemClick(item)
            }
        }
    }
}