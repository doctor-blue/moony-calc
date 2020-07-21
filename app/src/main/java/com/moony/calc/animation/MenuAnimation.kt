package com.moony.calc.animation

import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import com.moony.calc.views.MoonyFragment

object MenuAnimation {
    fun hideLeftMenu(content: MoonyFragment, leftMenu: LinearLayout) {
        val contentX = content.x
        val contentWidth = content.width
        val menuX = leftMenu.x
        val menWidth = leftMenu.width.toFloat()

        //content Animation
        ValueAnimator.ofFloat(contentX, contentX + menWidth).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                content.x = it.animatedValue as Float
            }
            start()
        }
        ValueAnimator.ofFloat(menuX, menuX + menWidth).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                leftMenu.x = it.animatedValue as Float
            }
            start()
        }

    }
    fun showLeftMenu(content: MoonyFragment, leftMenu: LinearLayout) {
        val contentX = content.x
        val menuX = leftMenu.x
        val menWidth = leftMenu.width.toFloat()

        //content Animation
        ValueAnimator.ofFloat(contentX, contentX - menWidth).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                content.x = it.animatedValue as Float
            }
            start()
        }
        ValueAnimator.ofFloat(menuX, menuX - menWidth).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                leftMenu.x = it.animatedValue as Float
            }
            start()
        }

    }
}