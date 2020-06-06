package com.moony.calc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.numberprogressbar.NumberProgressBar
import com.google.android.material.textview.MaterialTextView
import com.moony.calc.R
import com.moony.calc.model.Saving

class SavingBoxAdapter(
    private val context: Context,
    private val savings: List<Saving>
//    private val onClick: (Unit) -> Unit
) : RecyclerView.Adapter<SavingBoxAdapter.SavingViewHolder>() {

    inner class SavingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgGoalCategoryItem: ImageView = itemView.findViewById(R.id.img_goal_category_item)
        val txtSavingDescriptionItem: MaterialTextView =
            itemView.findViewById(R.id.txt_saving_description_item)
        val txtAmountProgressItem: MaterialTextView =
            itemView.findViewById(R.id.txt_amount_progress_item)
        val pbSavingGoalsItem: NumberProgressBar = itemView.findViewById(R.id.pb_saving_goals_item)

        fun onBind(saving:Saving){
            imgGoalCategoryItem.setImageResource(saving.idCategory)
            txtSavingDescriptionItem.text = saving.description
            txtAmountProgressItem.text = saving.desiredAmount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.saving_goals_item, parent, false)
        return SavingViewHolder(view)
    }

    override fun getItemCount(): Int = savings.size


    override fun onBindViewHolder(holder: SavingViewHolder, position: Int) {
        holder.onBind(savings[position])
    }
}