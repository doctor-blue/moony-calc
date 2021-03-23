package com.moony.calc.ui.saving

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingItem
import kotlinx.android.synthetic.main.fragment_saving_box.*

class SavingBoxFragment : BaseFragment() {
    private lateinit var savings: List<SavingItem>
    private val savingViewModel: SavingViewModel by lazy { ViewModelProvider(this)[SavingViewModel::class.java] }
    lateinit var adapter: SavingBoxAdapter

    companion object {
        const val SAVING_KEY = "com.moony.calc.ui.saving.SavingBoxFragment.SAVING_KEY"
    }

    override fun init() {
        initControl()
        initEvent()
    }

    private fun initEvent() {
        btn_add_saving_goals.setOnClickListener {
            findNavController().navigate(R.id.action_saving_fragment_to_addSavingGoalActivity)
        }
        adapter = SavingBoxAdapter(requireActivity(), countCompletedGoals) {
            val saving: Saving = it as Saving
            val intent = Intent(baseContext, SavingDetailActivity::class.java)
            intent.putExtra(SAVING_KEY, saving)
            startActivity(intent)
        }
    }

    private fun initControl() {
        rv_saving_box.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        savingViewModel.getAllSaving().observe(this,
            { savings ->
                this.savings = savings
                adapter.setSavings(savings)
                if (savings.isNotEmpty()) {
                    rv_saving_box.adapter = adapter
                    layout_list_empty.visibility = View.GONE
                    rv_saving_box.visibility = View.VISIBLE

                    txt_total_goal.text = "${savings.size}"
                    txt_total_completed.text = "$goalsCompleted"
                    txt_total_in_progress.text = "${savings.size - goalsCompleted}"
                } else {
                    layout_list_empty.visibility = View.VISIBLE
                    rv_saving_box.visibility = View.GONE
                }

            })
    }

    private var goalsCompleted: Int = 0
    private val countCompletedGoals: () -> Unit = {
        goalsCompleted++
        txt_total_completed.text = "$goalsCompleted"
        txt_total_in_progress.text = "${savings.size - goalsCompleted}"
    }

    override fun getLayoutId(): Int = R.layout.fragment_saving_box

}