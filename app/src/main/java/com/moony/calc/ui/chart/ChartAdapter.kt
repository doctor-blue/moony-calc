package com.moony.calc.ui.chart

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.moony.calc.R
import com.moony.calc.model.ChartItem
import com.moony.calc.model.TransactionItem
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings

class ChartAdapter(
    private val context: FragmentActivity,
    private val itemClick: (ChartItem) -> Unit
) : RecyclerView.Adapter<ChartAdapter.ViewHolder>() {

    private var chartItems: List<ChartItem> = listOf()
    private val settings = Settings.getInstance(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(chartItems[position], itemClick)
    }

    override fun getItemCount(): Int = chartItems.size

    fun refreshChartItems(chartItems: List<ChartItem>) {
        this.chartItems = chartItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgChartItem: ImageView = itemView.findViewById(R.id.img_transaction)
        private val txtCategoryName: TextView =
            itemView.findViewById(R.id.txt_transaction_name)
        private val cardChartItem: MaterialCardView =
            itemView.findViewById(R.id.card_transaction)
        private val txtChartItemMoney: TextView =
            itemView.findViewById(R.id.txt_transaction_money)
        private val txtPercent: TextView = itemView.findViewById(R.id.txt_transaction_date)

        fun onBind(chartItem: ChartItem, itemClick: (ChartItem) -> Unit) {
            Glide.with(context).load(AssetFolderManager.assetPath + chartItem.category.iconUrl)
                .into(imgChartItem)
            txtCategoryName.text = chartItem.category.title
            txtChartItemMoney.text = chartItem.sum.toString()
            txtPercent.text = ""
            cardChartItem.setOnClickListener {
                itemClick(chartItem)
            }
        }
    }




}