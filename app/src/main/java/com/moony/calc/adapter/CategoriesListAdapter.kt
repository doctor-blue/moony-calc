package com.moony.calc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.moony.calc.R
import com.moony.calc.utils.AssetFolderManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesListAdapter(private val titles: List<Int>, val context: Context) :
    RecyclerView.Adapter<CategoriesListAdapter.CategoriesListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false)
        return CategoriesListViewHolder(view)
    }

    override fun getItemCount(): Int = titles.size

    override fun onBindViewHolder(holder: CategoriesListViewHolder, position: Int) {
        holder.onBind(title = titles[position])
    }

    inner class CategoriesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitle=itemView.findViewById<MaterialTextView>(R.id.txt_category_list_title)
        private val rvCategory:RecyclerView=itemView.findViewById(R.id.rv_list_category)
        fun onBind(title:Int){
            txtTitle.setText(title)
            GlobalScope.launch(Dispatchers.IO) {
                val listIcon=AssetFolderManager.getAllCategories(AssetFolderManager.imageMap[title]!!).await()

                withContext(Dispatchers.Main){
                    val categoryAdapter= CategoryAdapter(context,listIcon,null)
                    rvCategory.setHasFixedSize(true)
                    rvCategory.layoutManager=GridLayoutManager(context,3)
                    rvCategory.adapter=categoryAdapter
                    /*rvCategory.apply {
                        setRecycledViewPool(recycledViewPool)
                    }*/
                }
            }
        }
    }
}