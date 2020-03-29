package com.moony.calc.adapter

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
import com.moony.calc.model.Transaction

/**
 * Đây là lớp con của List các Transaction
 */

class TransactionChildrenAdapter(
    private val transactions: List<Transaction>,
    private val itemClick: (Transaction) -> Unit,
    private val context: FragmentActivity
) : RecyclerView.Adapter<TransactionChildrenAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.transaction_children_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(transactions[position], itemClick)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgTransactionIcon: ImageView =
            itemView.findViewById(R.id.img_transaction_child)
        private val txtTransactionName: TextView =
            itemView.findViewById(R.id.txt_transaction_child_name)
        private val cardTransaction: MaterialCardView =
            itemView.findViewById(R.id.card_transaction_child)

        fun onBind(transaction: Transaction, itemClick: (Transaction) -> Unit) {
            val categoryViewModel =
                ViewModelProvider(context).get(CategoryViewModel::class.java)

            categoryViewModel.getCategory(transaction.idCategory).observe(context, Observer {
                Glide.with(context).load(it.iconUrl).into(imgTransactionIcon)
                txtTransactionName.text = it.title
            })

            cardTransaction.setOnClickListener {
                //thực hiện hàm click nội dung hàm sẽ được viết ở TransactionFragment
                itemClick(transaction)
            }
        }
    }
}