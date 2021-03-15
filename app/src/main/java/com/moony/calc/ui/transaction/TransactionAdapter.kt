package com.moony.calc.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.moony.calc.R
import com.moony.calc.model.Category
import com.moony.calc.model.Transaction
import com.moony.calc.model.TransactionItem
import com.moony.calc.ui.category.CategoryViewModel
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat

class TransactionAdapter(
    private val context: FragmentActivity,
    private val itemClick: (TransactionItem) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private var transactions: List<TransactionItem> = listOf()
    private val settings = Settings.getInstance(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(transactions[position], itemClick)
    }

    fun refreshTransactions(transactions: List<TransactionItem>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgTransactionIcon: ImageView =
            itemView.findViewById(R.id.img_transaction)
        private val txtTransactionName: TextView =
            itemView.findViewById(R.id.txt_transaction_name)
        private val cardTransaction: MaterialCardView =
            itemView.findViewById(R.id.card_transaction)
        private val txtTransactionMoney: TextView =
            itemView.findViewById(R.id.txt_transaction_money)
        private val txtDate: TextView = itemView.findViewById(R.id.txt_transaction_date)

        fun onBind(transactionItem: TransactionItem, itemClick: (TransactionItem) -> Unit) {
//            val categoryViewModel =
//                ViewModelProvider(context).get(CategoryViewModel::class.java)

//            categoryViewModel.getCategory(transaction.idCategory).observe(context, Observer {
//                if (it != null) {
//                    Glide.with(context).load(AssetFolderManager.assetPath + it.iconUrl)
//                        .into(imgTransactionIcon)
//                    txtTransactionName.text = it.title
//                    category = it
//                }
//            })

            Glide.with(context).load(AssetFolderManager.assetPath + transactionItem.category.iconUrl)
                .into(imgTransactionIcon)
            txtTransactionName.text = transactionItem.category.title

            if (transactionItem.category.isIncome)
                txtTransactionMoney.text =
                    (transactionItem.transaction.money.decimalFormat() + settings.getString(Settings.SettingKey.CURRENCY_UNIT))
            else
                txtTransactionMoney.text =
                    ((transactionItem.transaction.money * -1).decimalFormat() + settings.getString(Settings.SettingKey.CURRENCY_UNIT))

            txtDate.text = ("${transactionItem.transaction.day}/${transactionItem.transaction.month + 1}/${transactionItem.transaction.year}")

            cardTransaction.setOnClickListener {
                //thực hiện hàm click nội dung hàm sẽ được viết ở TransactionFragment
                itemClick(transactionItem)
            }
        }
    }
}