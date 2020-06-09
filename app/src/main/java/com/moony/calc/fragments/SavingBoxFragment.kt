package com.moony.calc.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moony.calc.R
import com.moony.calc.activities.AddSavingGoalActivity
import com.moony.calc.adapter.SavingBoxAdapter
import com.moony.calc.base.BaseFragment
import com.moony.calc.database.SavingViewModel
import com.moony.calc.model.Saving
import kotlinx.android.synthetic.main.fragment_saving_box.*

class SavingBoxFragment() : BaseFragment() {
    private lateinit var savings: List<Saving>
    private val savingViewModel: SavingViewModel by lazy { ViewModelProvider(this)[SavingViewModel::class.java] }
//    private val onClick: (Any) -> Unit


    override fun init() {
        initControl()
        initEvent()
    }

    private fun initEvent() {
        btn_add_saving_goals.setOnClickListener {
            startActivity(Intent(activity,AddSavingGoalActivity::class.java))
        }
    }

    private fun initControl() {
        rv_saving_box.layoutManager = LinearLayoutManager(context)
        savingViewModel.getAllSaving().observe(this,
            Observer { savings ->
                val adapter = SavingBoxAdapter(context!!, savings)
                rv_saving_box.adapter=adapter
            })
    }

    override fun getLayoutId(): Int = R.layout.fragment_saving_box

}