package com.moony.calc.ui.chart

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.devcomentry.moonlight.binding.BindingListAdapter
import com.devcomentry.moonlight.binding.BindingViewHolder
import com.faskn.lib.Slice
import com.moony.calc.R
import com.moony.calc.databinding.ChartLegendItemBinding


class ChartLegendAdapter: BindingListAdapter<Slice, ChartLegendItemBinding>(R.layout.chart_legend_item) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(getBinding(parent))
    }

    inner class ViewHolder(
            private val binding: ChartLegendItemBinding
    ) : BindingViewHolder<Slice, ChartLegendItemBinding>(binding) {

        override fun onBind(item: Slice) {
            binding.imgLegendCircle.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(itemView.context, item.color))
            binding.txtLegendName.text = item.name
        }

    }

}