package com.moony.calc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moony.calc.R
import com.moony.calc.model.Category
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.views.SVGImageView

class CategoryAdapter(val context: Context,val links:List<String>?,val categories:List<Category>?) :RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        private val imgIcon:SVGImageView=itemView.findViewById(R.id.img_category_item)
        private val txtCategoryName:TextView=itemView.findViewById(R.id.txt_category_item_name)

        fun onBind(links:String){
            imgIcon.setImageAsset(links)
            txtCategoryName.text=""
        }
        fun onBind(category: Category){
            imgIcon.setImageAsset(AssetFolderManager.assetPath+category.iconUrl)
            txtCategoryName.text=category.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_item,parent,false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = links?.size ?: categories!!.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        if (links!=null){
            holder.onBind(links[position])
        }else{
            holder.onBind(categories!![position])
        }
    }
}