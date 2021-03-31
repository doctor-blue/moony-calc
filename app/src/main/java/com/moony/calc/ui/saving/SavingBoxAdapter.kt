package com.moony.calc.ui.saving

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
import com.moony.calc.ui.category.CategoryViewModel
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingItem
import com.moony.calc.ui.saving.history.SavingHistoryViewModel
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat
import kotlin.math.floor

class SavingBoxAdapter(
    private val context: FragmentActivity,
    private val onGoalCompleted: () -> Unit,
    private val onClick: (Any) -> Unit
) : RecyclerView.Adapter<SavingViewHolder>() {
    private var savings: List<SavingItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.saving_goals_item, parent, false)
        return SavingViewHolder(view,context, onGoalCompleted, onClick)
    }

    override fun getItemCount(): Int = savings.size


    override fun onBindViewHolder(holder: SavingViewHolder, position: Int) {
        holder.onBind(savings[position])
    }

    fun setSavings(savings: List<SavingItem>) {
        this.savings = savings
        notifyDataSetChanged()
    }
}