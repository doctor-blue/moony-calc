package com.moony.calc.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.numberprogressbar.NumberProgressBar
import com.google.android.material.textview.MaterialTextView
import com.moony.calc.R
import com.moony.calc.model.Saving

class SavingBoxAdapter(private val context: Context, private val savings: List<Saving>, private val onClick: (Saving) -> Unit): RecyclerView.Adapter<SavingBoxAdapter.SavingViewHolder>() {

    inner class SavingViewHolder(itemView: View, private val onClick: (Saving) -> Unit): RecyclerView.ViewHolder(itemView) {

        val imgGoalCategoryItem: ImageView = itemView.findViewById(R.id.img_goal_category_item)
        val txtSavingDescriptionItem: MaterialTextView = itemView.findViewById(R.id.txt_saving_description_item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: SavingViewHolder, position: Int) {
        TODO("Not yet implemented")
    }



}