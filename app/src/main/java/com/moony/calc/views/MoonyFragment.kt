package com.moony.calc.views

import android.content.Context
import android.os.AsyncTask
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.moony.calc.base.BaseFragment

class MoonyFragment : FrameLayout {
     lateinit var fragmentManager: FragmentManager
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun replaceFragment(newFragment: BaseFragment) {
        ReplaceTask(fragmentManager,id).execute(newFragment)
    }
    private class ReplaceTask(private val fragmentManager: FragmentManager,private val id:Int):AsyncTask<BaseFragment,Unit,Unit>(){
        override fun doInBackground(vararg params: BaseFragment?) {
            fragmentManager.beginTransaction().replace(id, params[0]!!).commit()
            Log.d("TEST",params[0].toString())
        }

    }
}