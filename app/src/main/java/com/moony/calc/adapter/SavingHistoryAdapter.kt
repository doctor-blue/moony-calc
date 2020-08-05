package com.moony.calc.adapter

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
import com.moony.calc.database.CategoryViewModel
import com.moony.calc.model.SavingHistory
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.decimalFormat

class SavingHistoryAdapter(
    private val activity: FragmentActivity,
    private val histories: List<SavingHistory>,
    private val itemClick: (SavingHistory) -> Unit
) : RecyclerView.Adapter<SavingHistoryAdapter.SavingHistoryViewHolder>() {
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(activity)[CategoryViewModel::class.java]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingHistoryViewHolder {
        val view =
            LayoutInflater.from(activity).inflate(R.layout.saving_history_item, parent, false)
        return SavingHistoryViewHolder(view)
    }

    override fun getItemCount(): Int = histories.size

    override fun onBindViewHolder(holder: SavingHistoryViewHolder, position: Int) {
        holder.onBind(histories[position])
    }


    inner class SavingHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgCategory: ImageView = itemView.findViewById(R.id.img_saving_history_category)
        private val txtTitle: TextView =
            itemView.findViewById(R.id.txt_saving_history_title)
        private val txtMoney: TextView = itemView.findViewById(R.id.txt_saving_history_money)
        private val layoutContent =
            itemView.findViewById<LinearLayout>(R.id.layout_saving_history_item)

        fun onBind(savingHistory: SavingHistory) {
            categoryViewModel.getCategory(savingHistory.idCategory).observe(activity, Observer {
                Glide.with(activity).load(AssetFolderManager.assetPath + it.iconUrl)
                    .into(imgCategory)
            })

            txtMoney.text = savingHistory.amount.decimalFormat()
            if (savingHistory.isSaving) {
                txtMoney.setTextColor(activity.resources.getColor(R.color.blue))
            } else {
                txtMoney.setTextColor(activity.resources.getColor(R.color.colorAccent))
            }
            if (savingHistory.description.length > 10) {
                txtTitle.text = (savingHistory.description.substring(0, 10) + "...")
            } else {
                txtTitle.text = savingHistory.description
            }
            layoutContent.setOnClickListener {
                itemClick(savingHistory)
            }
        }
    }
}