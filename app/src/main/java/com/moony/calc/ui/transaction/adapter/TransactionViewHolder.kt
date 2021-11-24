package com.moony.calc.ui.transaction.adapter

import com.bumptech.glide.Glide
import com.devcomentry.moonlight.binding.BindingViewHolder
import com.moony.calc.databinding.TransactionItemBinding
import com.moony.calc.model.TransactionItem
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.formatDateTime

class TransactionViewHolder(
    itemClick: (TransactionItem) -> Unit,
    private val binding: TransactionItemBinding
) :
    BindingViewHolder<TransactionItem, TransactionItemBinding>(binding) {
    private val settings = Settings.getInstance(itemView.context)

    private var transactionItem: TransactionItem? = null

    init {
        binding.cardTransaction.setOnClickListener {
            //thực hiện hàm - click nội dung hàm sẽ được viết ở TransactionFragment
            transactionItem?.let {
                itemClick(it)
            }
        }
    }

    override fun onBind(item: TransactionItem) {
        this.transactionItem = item
        Glide.with(itemView.context)
            .load(AssetFolderManager.assetPath + item.category.iconUrl)
            .into(binding.imgTransaction)

        binding.txtTransactionName.text = item.category.title

        if (item.category.isIncome)
            binding.txtTransactionMoney.text =
                (item.transaction.money.decimalFormat() + settings.getString(Settings.SettingKey.CURRENCY_UNIT))
        else
            binding.txtTransactionMoney.text =
                ((item.transaction.money * -1).decimalFormat() + settings.getString(
                    Settings.SettingKey.CURRENCY_UNIT
                ))

        binding.txtTransactionDate.text = item.transaction.transactionTime.formatDateTime()

    }

}