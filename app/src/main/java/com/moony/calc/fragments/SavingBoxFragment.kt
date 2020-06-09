package com.moony.calc.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moony.calc.R
import com.moony.calc.activities.AddSavingGoalActivity
import com.moony.calc.activities.SavingDetailActivity
import com.moony.calc.adapter.SavingBoxAdapter
import com.moony.calc.base.BaseFragment
import com.moony.calc.database.SavingViewModel
import com.moony.calc.model.Saving
import kotlinx.android.synthetic.main.fragment_saving_box.*

class SavingBoxFragment() : BaseFragment() {
    private lateinit var savings: List<Saving>
    private val savingViewModel: SavingViewModel by lazy { ViewModelProvider(this)[SavingViewModel::class.java] }


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
                if(savings.isNotEmpty()) {
                    val adapter = SavingBoxAdapter(activity!!, savings) {
                        val saving: Saving = it as Saving
                        val intent: Intent = Intent(baseContext, SavingDetailActivity::class.java)
                        intent.putExtra("SavingDescription", saving.description)
                        intent.putExtra("SavingAmount", saving.desiredAmount)
                        intent.putExtra("SavingDeadLine", saving.deadLine)
                        intent.putExtra("IDCategory", saving.idCategory)
                        startActivity(intent)
                    }
                    rv_saving_box.adapter=adapter
                    txt_add_goal_manual.visibility = View.GONE
                    txt_empty_goal.visibility = View.GONE
                    rv_saving_box.visibility = View.VISIBLE
                } else {
                    txt_add_goal_manual.visibility = View.VISIBLE
                    txt_empty_goal.visibility = View.VISIBLE
                    rv_saving_box.visibility = View.GONE
                }

            })
    }

    override fun getLayoutId(): Int = R.layout.fragment_saving_box

}