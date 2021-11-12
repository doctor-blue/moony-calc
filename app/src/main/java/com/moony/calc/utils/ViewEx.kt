package com.moony.calc.utils

import android.graphics.Rect
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View


fun View.isShow() = visibility == View.VISIBLE

fun View.isGone() = visibility == View.GONE

fun View.isInvisible() = visibility == View.INVISIBLE

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.inv() {
    visibility = View.INVISIBLE
}

fun View.setPreventDoubleClick(debounceTime: Long = 500, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.setPreventDoubleClickScaleView(debounceTime: Long = 500, action: () -> Unit) {
    setOnTouchListener(object : View.OnTouchListener {
        private var lastClickTime: Long = 0
        private var rect: Rect? = null

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            fun setScale(scale: Float) {
                v.scaleX = scale
                v.scaleY = scale
            }

            if (event.action == MotionEvent.ACTION_DOWN) {
                //action down: scale view down
                rect = Rect(v.left, v.top, v.right, v.bottom)
                setScale(0.9f)
            } else if (rect != null && !rect!!.contains(
                    v.left + event.x.toInt(),
                    v.top + event.y.toInt()
                )
            ) {
                //action moved out
                setScale(1f)
                return false
            } else if (event.action == MotionEvent.ACTION_UP) {
                //action up
                setScale(1f)
                //handle click too fast
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                } else {
                    lastClickTime = SystemClock.elapsedRealtime()
                    action()
                }
            } else {
                //other
            }

            return true
        }
    })
}