package com.moony.calc.ui.saving

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daimajia.numberprogressbar.NumberProgressBar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.moony.calc.R
import com.moony.calc.model.Saving
import com.moony.calc.ui.saving.history.SavingHistoryViewModel
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat
import kotlin.math.floor

class SavingViewHolder(
    itemView: View,
    private val context: FragmentActivity,
    private val onClick: (Any) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val imgGoalCategoryItem: ImageView =
        itemView.findViewById(R.id.img_goal_category_item)
    private val txtSavingDescriptionItem: MaterialTextView =
        itemView.findViewById(R.id.txt_saving_description_item)
    private val txtAmountProgressItem: MaterialTextView =
        itemView.findViewById(R.id.txt_amount_progress_item)
    private val pbSavingGoalsItem: NumberProgressBar =
        itemView.findViewById(R.id.pb_saving_goals_item)
    private val cardSavingBox: MaterialCardView = itemView.findViewById(R.id.card_saving_box)

    private val settings = Settings.getInstance(context)
    private var saving: Saving? = null

    init {
        cardSavingBox.setOnClickListener {
            saving?.let {
                onClick(it)
            }
        }
    }

    fun onBind(saving: Saving) {
        this.saving = saving

        Glide.with(context).load(AssetFolderManager.assetPath + saving.iconUrl)
            .into(imgGoalCategoryItem)

        val savingHistoryViewModel =
            ViewModelProvider(context)[SavingHistoryViewModel::class.java]
        savingHistoryViewModel.getCurrentAmount(saving.idSaving)
            .observe(context, {
                var saved = it
                if (saved == null) saved = 0.0

                var progress = floor(saved / saving.desiredAmount * 100).toInt()

                if (progress < 0) progress = 0
                if (progress > 100) progress = 100

                pbSavingGoalsItem.progress = progress
            })


        txtSavingDescriptionItem.text = saving.title
        txtAmountProgressItem.text =
            (saving.desiredAmount.decimalFormat() + settings.getString(Settings.SettingKey.CURRENCY_UNIT))

    }
}