package com.moony.calc.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.model.Category
import com.moony.calc.ui.dialog.ConfirmDialogBuilder
import com.moony.calc.ui.transaction.TransactionViewModel

class CategoryAdapter(
    val context: Context,
    private val links: List<String>?,
    private val categories: List<Category>?,
    private val canRemove: Boolean,
    private val onClick: (Any) -> Unit

) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private val categoryViewModel by lazy {
        ViewModelProvider(context as FragmentActivity).get(CategoryViewModel::class.java)
    }

    private val transactionViewModel by lazy {
        ViewModelProvider(context as FragmentActivity).get(TransactionViewModel::class.java)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgIcon: ImageView = itemView.findViewById(R.id.img_category_item)
        private val txtCategoryName: TextView = itemView.findViewById(R.id.txt_category_item_name)
        private val btnRemoveCategory: ImageView = itemView.findViewById(R.id.btn_remove_category)

        fun onBind(link: String, onClick: (Any) -> Unit) {
            Glide.with(context).load("//android_asset/$link").into(imgIcon)
            txtCategoryName.text = ""
            imgIcon.setOnClickListener {
                onClick(link)
            }
        }

        fun onBind(category: Category, onClick: (Any) -> Unit) {
            val categoryTitle =
                if (category.resId != -1) txtCategoryName.context.resources.getString(category.resId) else category.title
            if (category.title.length >= 10) {
                txtCategoryName.text = (categoryTitle.substring(0, 10) + "...")
            } else {
                txtCategoryName.text = categoryTitle
            }

            if (category.title == context.resources.getText(R.string.add)) {
                Glide.with(context).load(R.drawable.ic_add).into(imgIcon)
                btnRemoveCategory.visibility = View.GONE
            } else {
                Glide.with(context).load("//android_asset/${category.iconUrl}").into(imgIcon)
                if (canRemove) {
                    btnRemoveCategory.visibility = View.VISIBLE
                    btnRemoveCategory.setOnClickListener {
                        val builder = ConfirmDialogBuilder(context)
                        builder.setContent(context.resources.getString(R.string.notice_delete_category))
                        val dialog = builder.createDialog()

                        builder.btnConfirm.setOnClickListener {

                            transactionViewModel.deleteAllTransactionByCategory(category.idCategory)
                            categoryViewModel.deleteCategory(category)
                            dialog.dismiss()

                        }
                        builder.btnCancel.setOnClickListener { dialog.dismiss() }
                        builder.showDialog()

                    }
                }
            }
            imgIcon.setOnClickListener {
                onClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = links?.size ?: categories!!.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        if (links != null) {
            holder.onBind(links[position], onClick)
        } else {
            holder.onBind(categories!![position], onClick)
        }
    }
}