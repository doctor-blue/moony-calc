package com.moony.calc.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.card.MaterialCardView
import com.moony.calc.R

class NavigationMenu : LinearLayout {
    private lateinit var cardBudget: MaterialCardView
    private lateinit var cardSaving: MaterialCardView
    private lateinit var cardCategories: MaterialCardView
    private lateinit var cardChart: MaterialCardView
    lateinit var itemClick: NavigationItemClick

    constructor(context: Context) : this(context, null) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun init(context: Context) {
        orientation = VERTICAL
        val rootView = View.inflate(context, R.layout.menu_view, this)
        cardBudget = rootView.findViewById(R.id.card_budget)
        cardSaving = rootView.findViewById(R.id.card_saving)
        cardCategories = rootView.findViewById(R.id.card_categories)
        cardChart = findViewById(R.id.card_chart)

        cardBudget.setOnClickListener {
            resetCardBackgroundColor()
            setCardBackgroundClick(cardBudget,context)
            itemClick.onClick(it)
        }
        cardSaving.setOnClickListener {
            resetCardBackgroundColor()
            setCardBackgroundClick(cardSaving,context)
            itemClick.onClick(it)
        }
        cardCategories.setOnClickListener {
            resetCardBackgroundColor()
            setCardBackgroundClick(cardCategories,context)
            itemClick.onClick(it)
        }
        cardChart.setOnClickListener {
            resetCardBackgroundColor()
            setCardBackgroundClick(cardChart,context)
            itemClick.onClick(it)
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun resetCardBackgroundColor() {
        cardBudget.setCardBackgroundColor(android.R.color.transparent)
        cardSaving.setCardBackgroundColor(android.R.color.transparent)
        cardChart.setCardBackgroundColor(android.R.color.transparent)
        cardCategories.setCardBackgroundColor(android.R.color.transparent)
    }

    @SuppressLint("ResourceType")
    private fun setCardBackgroundClick(cardView: MaterialCardView, context: Context) {
        cardView.setCardBackgroundColor(context.resources.getColor(R.color.green_100))
    }

}