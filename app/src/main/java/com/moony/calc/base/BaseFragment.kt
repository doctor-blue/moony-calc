package com.moony.calc.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


abstract class BaseFragment : Fragment() {

    protected var fragmentActivity: FragmentActivity? = null
    protected  var baseContext: Context?=null

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(getLayoutId(), container, false)
        fragmentActivity = activity
        baseContext = activity
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    abstract fun init()
    abstract fun getLayoutId(): Int
}
