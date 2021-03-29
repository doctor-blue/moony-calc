package com.moony.calc.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


abstract class BaseFragment : Fragment() {
    private lateinit var binding: ViewDataBinding

    protected var fragmentActivity: FragmentActivity? = null
    protected  var baseContext: Context?=null

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        val view: View = inflater.inflate(getLayoutId(), container, false)
        fragmentActivity = activity
        baseContext = activity
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initControls(view, savedInstanceState)
        initEvents()
    }

    open fun init(){}

    abstract fun getLayoutId(): Int

    open fun initControls(view: View, savedInstanceState: Bundle?){}

    open fun initEvents(){}

    fun getViewBinding():ViewDataBinding{
        return binding
    }
}
