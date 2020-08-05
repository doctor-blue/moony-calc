package com.moony.calc.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.moony.calc.base.BaseFragment

class MoonyFragment : FrameLayout {
    lateinit var fragmentManager: FragmentManager
    private var currentFragment: BaseFragment? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun replaceFragment(newFragment: BaseFragment) {
        if (currentFragment !== newFragment) {
            Log.d(MoonyFragment::class.java.simpleName, "${currentFragment?.id}  ${newFragment.id}")
            currentFragment = newFragment
            fragmentManager.beginTransaction().replace(id, newFragment).commit()
        }

    }
}