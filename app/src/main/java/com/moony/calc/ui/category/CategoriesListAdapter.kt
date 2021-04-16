package com.moony.calc.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.moony.calc.R
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.GridSpacingItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesListAdapter(
    private val titles: List<Int>,
    val context: Context,
    val onClick: (String, Int) -> Unit
) :
    RecyclerView.Adapter<CategoriesListAdapter.CategoriesListViewHolder>() {
    private var viewPool = RecyclerView.RecycledViewPool()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false)
        return CategoriesListViewHolder(view)
    }

    override fun getItemCount(): Int = titles.size

    override fun onBindViewHolder(holder: CategoriesListViewHolder, position: Int) {
        holder.onBind(title = titles[position])
    }

    inner class CategoriesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitle = itemView.findViewById<MaterialTextView>(R.id.txt_category_list_title)
        private val rvCategory: RecyclerView = itemView.findViewById(R.id.rv_list_category)
        fun onBind(title: Int) {
            txtTitle.setText(title)
            GlobalScope.launch(Dispatchers.IO) {
                val listIcon =
                    AssetFolderManager.getAllCategories(AssetFolderManager.imageMap[title]!!)
                        .await()

                withContext(Dispatchers.Main) {
                    val categoryAdapter = CategoryAdapter(context, false) {
                        onClick(it as String, title)
                    }
                    categoryAdapter.setLinks(listIcon)
                    rvCategory.setHasFixedSize(true)
                    rvCategory.layoutManager = GridLayoutManager(context, 4)
                    rvCategory.adapter = categoryAdapter
                    rvCategory.apply {
                        setRecycledViewPool(viewPool)
                        addItemDecoration(
                            GridSpacingItemDecoration(
                                4,
                                context.resources.getDimensionPixelOffset(R.dimen._15sdp),
                                true
                            )
                        )
                    }
                }
            }
        }
    }
}