package com.moony.calc.ui.adapter.viewholder

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.devcomentry.moonlight.binding.BindingViewHolder
import com.moony.calc.databinding.SavingGoalsItemBinding
import com.moony.calc.model.Saving
import com.moony.calc.ui.saving.history.SavingHistoryViewModel
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat
import kotlin.math.floor

class SavingViewHolder(
        private val context: FragmentActivity,
        private val onClick: (Any) -> Unit,
        private val binding: SavingGoalsItemBinding,
) : BindingViewHolder<Saving, SavingGoalsItemBinding>(binding) {

    private val settings = Settings.getInstance(context)
    private var saving: Saving? = null

    init {
        binding.cardSavingBox.setOnClickListener {
            saving?.let {
                onClick(it)
            }
        }
    }

   override fun onBind(item: Saving) {
        this.saving = item

        Glide.with(context).load(AssetFolderManager.assetPath + item.iconUrl)
                .into(binding.imgGoalCategoryItem)

        val savingHistoryViewModel =
                ViewModelProvider(context)[SavingHistoryViewModel::class.java]
        savingHistoryViewModel.getCurrentAmount(item.idSaving)
                .observe(context, {
                    var saved = it
                    if (saved == null) saved = 0.0

                    var progress = floor(saved / item.desiredAmount * 100).toInt()

                    if (progress < 0) progress = 0
                    if (progress > 100) progress = 100

                    binding.pbSavingGoalsItem.progress = progress
                })


        binding.txtSavingDescriptionItem.text = item.title
        binding.txtAmountProgressItem.text =
                (item.desiredAmount.decimalFormat() + settings.getString(Settings.SettingKey.CURRENCY_UNIT))

    }
}