package com.moony.calc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.moony.calc.database.SavingHistoryViewModel
import com.moony.calc.model.Category
import com.moony.calc.model.Saving
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat
import kotlin.math.floor

class SavingBoxAdapter(
    private val context: FragmentActivity,
    private val onGoalCompleted: () -> Unit,
    private val onClick: (Any) -> Unit
) : RecyclerView.Adapter<SavingBoxAdapter.SavingViewHolder>() {
    private val settings = Settings.getInstance(context)
    private var savings: List<Saving> = listOf()

    inner class SavingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgGoalCategoryItem: ImageView =
            itemView.findViewById(R.id.img_goal_category_item)
        private val txtSavingDescriptionItem: MaterialTextView =
            itemView.findViewById(R.id.txt_saving_description_item)
        private val txtAmountProgressItem: MaterialTextView =
            itemView.findViewById(R.id.txt_amount_progress_item)
        private val pbSavingGoalsItem: NumberProgressBar =
            itemView.findViewById(R.id.pb_saving_goals_item)
        private val cardSavingBox: MaterialCardView = itemView.findViewById(R.id.card_saving_box)

        fun onBind(saving: Saving, onClick: (Any) -> Unit) {
            val categoryViewModel =
                ViewModelProvider(context).get(CategoryViewModel::class.java)

            categoryViewModel.getCategory(saving.idCategory).observe(context, Observer { category ->
                Glide.with(context).load(AssetFolderManager.assetPath + category.iconUrl)
                    .into(imgGoalCategoryItem)
            })
            val savingHistoryViewModel =
                ViewModelProvider(context)[SavingHistoryViewModel::class.java]
            savingHistoryViewModel.getCurrentAmount(saving.idSaving).observe(context, Observer {
                var saved = it
                if (saved == null) saved = 0.0

                var progress = floor(saved / saving.desiredAmount * 100).toInt()

                if (progress < 0) progress = 0
                if (progress > 100) progress = 100
                if (progress==100){
                    onGoalCompleted()
                }


                pbSavingGoalsItem.progress = progress
            })


            txtSavingDescriptionItem.text = saving.description
            txtAmountProgressItem.text =
                (saving.desiredAmount.decimalFormat() + settings.getString(Settings.SettingKey.CURRENCY_UNIT))

            cardSavingBox.setOnClickListener {
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

    fun setSavings(savings: List<Saving>) {
        this.savings = savings
        notifyDataSetChanged()
    }
}