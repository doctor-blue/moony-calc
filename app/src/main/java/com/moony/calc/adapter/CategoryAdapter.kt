package com.moony.calc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.model.Category

class CategoryAdapter(
    val context: Context,
    private val links: List<String>?,
    private val categories: List<Category>?,
    private val onClick: (Any) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgIcon: ImageView = itemView.findViewById(R.id.img_category_item)
        private val txtCategoryName: TextView = itemView.findViewById(R.id.txt_category_item_name)

        fun onBind(links: String, onClick: (Any) -> Unit) {
            Glide.with(context).load("//android_asset/$links").into(imgIcon)
            txtCategoryName.text = ""
            imgIcon.setOnClickListener {
                onClick(links)
            }
        }

        fun onBind(category: Category, onClick: (Any) -> Unit) {
            txtCategoryName.text = category.title
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