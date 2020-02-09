package com.moony.calc.base

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.annotation.NonNull
import android.app.Activity
import android.view.View


abstract class BaseFragment() : Fragment() {

    var activity: Activity? = null

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        var view:View? = inflater.inflate(getLayoutId(), container, false)
        activity = getActivity()
        init()
        return view
    }

    abstract fun init()
    abstract fun getLayoutId(): Int
    override fun onDestroy() {
        super.onDestroy()
    }
}
