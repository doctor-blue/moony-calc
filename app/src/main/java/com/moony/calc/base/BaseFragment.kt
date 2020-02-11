package com.moony.calc.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


abstract class BaseFragment() : Fragment() {

    var activity: Activity? = null

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = inflater.inflate(getLayoutId(), container, false)
        activity = getActivity()
        init()
        return view
    }

    abstract fun init()
    abstract fun getLayoutId(): Int
}
