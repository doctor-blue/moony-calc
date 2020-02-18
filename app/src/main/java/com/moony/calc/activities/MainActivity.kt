package com.moony.calc.activities

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override fun init(savedInstanceState: Bundle?) {
        initControl()
        initEvent()
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun initEvent() {
        /*btn_nav_main.setOnClickListener {
           // Log.d("Hello","${main_root.currentState}")
            if (main_root.currentState == R.id.start_main_transition) {
                //Log.d("Hello","true")
                main_root.setTransition(R.id.start_main_transition, R.id.end_main_transition)
            } else if (main_root.currentState == R.id.end_main_transition) {
                main_root.setTransition(R.id.end_main_transition, R.id.start_main_transition)
            }
            main_root.setTransitionDuration(1000)
            main_root.transitionToEnd()

        }*/

        main_root.setTransitionListener(object :TransitionAdapter(){
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                super.onTransitionCompleted(motionLayout, currentId)

                if (main_root.currentState == R.id.start_main_transition) {
                    Log.d("Hello","true")
                } else if (main_root.currentState == R.id.end_main_transition) {
                    Log.d("Hello","false")
                }
                else{
                    Log.d("Hello","nothing")
                }
            }
        })
    }

    private fun initControl() {

    }


}

