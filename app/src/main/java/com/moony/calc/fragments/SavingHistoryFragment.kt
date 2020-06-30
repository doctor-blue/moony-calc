package com.moony.calc.fragments

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_saving_history.*

class SavingHistoryFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_saving_history

    override fun init() {
        initControls()
        initEvent()
    }

    private fun initEvent() {
        rv_saving_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && btn_import_history.isShown) btn_import_history.visibility =
                    View.GONE
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) btn_import_history.visibility =
                    View.VISIBLE
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun initControls() {

    }



}