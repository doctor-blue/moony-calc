package com.moony.calc.ui.chart

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.faskn.lib.Slice
import com.moony.calc.R


class ChartLegendAdapter(
    private val context: FragmentActivity,
) : RecyclerView.Adapter<ChartLegendAdapter.ViewHolder>() {

    private var slices: ArrayList<Slice> = arrayListOf()
    private var colors = arrayListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chart_legend_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(slices[position])
    }

    override fun getItemCount(): Int = slices.size

    fun refreshLegend(slices: ArrayList<Slice>) {
        this.slices = slices
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgLegendCircle: ImageView = itemView.findViewById(R.id.img_legend_circle)
        private val txtLegendName: TextView = itemView.findViewById(R.id.txt_legend_name)

        fun onBind(slice: Slice) {
            imgLegendCircle.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(itemView.context, slice.color))
            txtLegendName.text = slice.name
        }

    }

}