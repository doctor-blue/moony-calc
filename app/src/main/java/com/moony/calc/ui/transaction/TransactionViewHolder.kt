package com.moony.calc.ui.transaction

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.moony.calc.R
import com.moony.calc.model.TransactionItem
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat

class TransactionViewHolder(itemView:View, itemClick: (TransactionItem) -> Unit)  : RecyclerView.ViewHolder(itemView) {
    private val imgTransactionIcon: ImageView =
        itemView.findViewById(R.id.img_transaction)
    private val txtTransactionName: TextView =
        itemView.findViewById(R.id.txt_transaction_name)
    private val cardTransaction: MaterialCardView =
        itemView.findViewById(R.id.card_transaction)
    private val txtTransactionMoney: TextView =
        itemView.findViewById(R.id.txt_transaction_money)
    private val txtDate: TextView = itemView.findViewById(R.id.txt_transaction_date)
    private val settings = Settings.getInstance(itemView.context)

    private var transactionItem: TransactionItem? = null

    init {
        cardTransaction.setOnClickListener {
            //thực hiện hàm click nội dung hàm sẽ được viết ở TransactionFragment
            transactionItem?.let {
                itemClick(it)
            }
        }
    }

    fun onBind(transactionItem: TransactionItem){
        this.transactionItem = transactionItem
        Glide.with(itemView.context).load(AssetFolderManager.assetPath + transactionItem.category.iconUrl)
            .into(imgTransactionIcon)
        txtTransactionName.text = transactionItem.category.title

        if (transactionItem.category.isIncome)
            txtTransactionMoney.text =
                (transactionItem.transaction.money.decimalFormat() + settings.getString(Settings.SettingKey.CURRENCY_UNIT))
        else
            txtTransactionMoney.text =
                ((transactionItem.transaction.money * -1).decimalFormat() + settings.getString(
                    Settings.SettingKey.CURRENCY_UNIT))

        txtDate.text = ("${transactionItem.transaction.day}/${transactionItem.transaction.month + 1}/${transactionItem.transaction.year}")

    }

}