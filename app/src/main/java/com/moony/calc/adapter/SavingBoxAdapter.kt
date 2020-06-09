package com.moony.calc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daimajia.numberprogressbar.NumberProgressBar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.moony.calc.R
import com.moony.calc.database.CategoryViewModel
import com.moony.calc.model.Category
import com.moony.calc.model.Saving
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.decimalFormat

class SavingBoxAdapter(
    private val context: FragmentActivity,
    private val savings: List<Saving>,
    private val onClick: (Any) -> Unit
) : RecyclerView.Adapter<SavingBoxAdapter.SavingViewHolder>() {

    inner class SavingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgGoalCategoryItem: ImageView = itemView.findViewById(R.id.img_goal_category_item)
        val txtSavingDescriptionItem: MaterialTextView =
            itemView.findViewById(R.id.txt_saving_description_item)
        val txtAmountProgressItem: MaterialTextView =
            itemView.findViewById(R.id.txt_amount_progress_item)
        val pbSavingGoalsItem: NumberProgressBar = itemView.findViewById(R.id.pb_saving_goals_item)
        val cardSavingBox: MaterialCardView = itemView.findViewById(R.id.card_saving_box)

        fun onBind(saving:Saving, onClick: (Any) -> Unit){
            val categoryViewModel =
                ViewModelProvider(context).get(CategoryViewModel::class.java)
            var category: Category = Category("Test", "URL", true)
            categoryViewModel.getCategory(saving.idCategory).observe(context, Observer{category ->
                Glide.with(context).load(AssetFolderManager.assetPath + category.iconUrl).into(imgGoalCategoryItem)
            })
            txtSavingDescriptionItem.text = saving.description
            txtAmountProgressItem.text = saving.desiredAmount.decimalFormat()

            cardSavingBox.setOnClickListener{
                onClick(saving)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.saving_goals_item, parent, false)
        return SavingViewHolder(view)
    }

    override fun getItemCount(): Int = savings.size


    override fun onBindViewHolder(holder: SavingViewHolder, position: Int) {
        holder.onBind(savings[position], onClick)
    }
}