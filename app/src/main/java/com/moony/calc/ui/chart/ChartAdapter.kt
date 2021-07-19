package com.moony.calc.ui.chart

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.devcomentry.moonlight.binding.BindingListAdapter
import com.devcomentry.moonlight.binding.BindingViewHolder
import com.moony.calc.R
import com.moony.calc.databinding.TransactionItemBinding
import com.moony.calc.model.ChartItem
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat

class ChartAdapter(
        private val itemClick: (ChartItem) -> Unit
) : BindingListAdapter<ChartItem, TransactionItemBinding>(R.layout.transaction_item) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(getBinding(parent))
    }

    inner class ViewHolder(
            private val binding: TransactionItemBinding
    ) : BindingViewHolder<ChartItem, TransactionItemBinding>(binding) {
        private var chartItem: ChartItem? = null
        private val settings = Settings.getInstance(binding.root.context)

        init {

            binding.cardTransaction.setOnClickListener {
                chartItem?.let {
                    itemClick(it)
                }
            }
        }

        override fun onBind(item: ChartItem) {
            chartItem = item

            Glide.with(binding.root.context).load(AssetFolderManager.assetPath + item.category.iconUrl)
                    .into(binding.imgTransaction)

            if (item.category.resId != -1) {
                binding.txtTransactionName.setText(item.category.resId)
            } else {
                binding.txtTransactionName.text = item.category.title
            }
            binding.txtTransactionMoney.text = (item.sum.decimalFormat() + settings.getString(Settings.SettingKey.CURRENCY_UNIT))
            binding.txtTransactionDate.text = ""
        }
    }


}