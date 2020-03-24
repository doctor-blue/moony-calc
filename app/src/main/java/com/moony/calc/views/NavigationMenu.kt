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
    private lateinit var viewContext: Context

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
        viewContext=context
        val rootView = View.inflate(context, R.layout.menu_view, this)
        cardBudget = rootView.findViewById(R.id.card_budget)
        cardSaving = rootView.findViewById(R.id.card_saving)
        cardCategories = rootView.findViewById(R.id.card_categories)
        cardChart = findViewById(R.id.card_chart)
        resetCardBackgroundColor()
        setCardBackgroundClick(cardBudget,context)

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
        cardBudget.setCardBackgroundColor(context.resources.getColor(R.color.white))
        cardSaving.setCardBackgroundColor(context.resources.getColor(R.color.white))
        cardChart.setCardBackgroundColor(context.resources.getColor(R.color.white))
        cardCategories.setCardBackgroundColor(context.resources.getColor(R.color.white))
    }

    @SuppressLint("ResourceType")
    private fun setCardBackgroundClick(cardView: MaterialCardView, context: Context) {
        cardView.setCardBackgroundColor(context.resources.getColor(R.color.light_blue))
    }

}