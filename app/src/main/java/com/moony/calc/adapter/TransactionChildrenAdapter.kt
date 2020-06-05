package com.moony.calc.adapter

import android.graphics.Color
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
import com.moony.calc.database.CategoryViewModel
import com.moony.calc.model.Category
import com.moony.calc.model.DateTime
import com.moony.calc.model.Transaction
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.decimalFormat

/**
 * Đây là lớp con của List các Transaction
 */

class TransactionChildrenAdapter(
    private val transactions: List<Transaction>,
    private val itemClick: (Transaction, DateTime, Category) -> Unit,
    private val context: FragmentActivity,
    private val dateTime: DateTime
) : RecyclerView.Adapter<TransactionChildrenAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.transaction_children_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(transactions[position], itemClick, dateTime)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgTransactionIcon: ImageView =
            itemView.findViewById(R.id.img_transaction_child)
        private val txtTransactionName: TextView =
            itemView.findViewById(R.id.txt_transaction_child_name)
        private val cardTransaction: MaterialCardView =
            itemView.findViewById(R.id.card_transaction_child)
        private val txtTransactionMoney: TextView =
            itemView.findViewById(R.id.txt_transaction_child_money)

        fun onBind(
            transaction: Transaction,
            itemClick: (Transaction, DateTime, Category) -> Unit,
            dateTime: DateTime
        ) {
            val categoryViewModel =
                ViewModelProvider(context).get(CategoryViewModel::class.java)

            txtTransactionMoney.text = transaction.money.decimalFormat()
            if (transaction.isIncome)
                txtTransactionMoney.setTextColor(Color.parseColor("#00A8E8"))
            else
                txtTransactionMoney.setTextColor(Color.parseColor("#fd6f43"))

            var category: Category = Category("Test", "URL", true)

             categoryViewModel.getCategory(transaction.idCategory).observe(context, Observer {
                 Glide.with(context).load(AssetFolderManager.assetPath+it.iconUrl).into(imgTransactionIcon)
                txtTransactionName.text = it.title
                category=it
            })

            cardTransaction.setOnClickListener {
                //thực hiện hàm click nội dung hàm sẽ được viết ở TransactionFragment
                itemClick(transaction, dateTime, category)
            }
        }
    }
}