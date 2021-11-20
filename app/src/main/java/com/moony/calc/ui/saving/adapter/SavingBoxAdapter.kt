package com.moony.calc.ui.saving.adapter

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.devcomentry.moonlight.binding.BindingListAdapter
import com.moony.calc.R
import com.moony.calc.databinding.SavingGoalsItemBinding
import com.moony.calc.model.Saving
import com.moony.calc.ui.saving.adapter.SavingViewHolder

class SavingBoxAdapter(
        private val context: FragmentActivity,
        private val onClick: (Any) -> Unit
) : BindingListAdapter<Saving, SavingGoalsItemBinding>(R.layout.saving_goals_item) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingViewHolder {
        val binding = getBinding(parent)
        return SavingViewHolder(context, onClick, binding)
    }
}