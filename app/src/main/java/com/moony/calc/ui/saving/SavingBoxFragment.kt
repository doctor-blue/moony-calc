package com.moony.calc.ui.saving

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentSavingBoxBinding
import com.moony.calc.model.Saving

class SavingBoxFragment : BaseFragment() {
    private lateinit var savings: List<Saving>
    private val savingViewModel: SavingViewModel by lazy { ViewModelProvider(this)[SavingViewModel::class.java] }
    private val adapter: SavingBoxAdapter by lazy {
        SavingBoxAdapter(requireActivity(), countCompletedGoals) {
            val saving: Saving = it as Saving
            val intent = Intent(baseContext, SavingDetailActivity::class.java)
            intent.putExtra(SAVING_KEY, saving)
            startActivity(intent)
        }
    }

    companion object {
        const val SAVING_KEY = "com.moony.calc.ui.saving.SavingBoxFragment.SAVING_KEY"
    }

    private val binding: FragmentSavingBoxBinding
        get() = (getViewBinding() as FragmentSavingBoxBinding)


    override fun initEvents() {
        binding.btnAddSavingGoals.setOnClickListener {
            findNavController().navigate(R.id.action_saving_fragment_to_addSavingGoalActivity)
        }
    }

    override fun initControls(view: View, savedInstanceState: Bundle?) {
        binding.rvSavingBox.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSavingBox.adapter = adapter

        savingViewModel.getAllSaving().observe(this,
            { savings ->
                this.savings = savings
                adapter.setSavings(savings)

                if (savings.isNotEmpty()) {
                    binding.layoutListEmpty.visibility = View.GONE
                    binding.rvSavingBox.visibility = View.VISIBLE
                } else {
                    binding.layoutListEmpty.visibility = View.VISIBLE
                    binding.rvSavingBox.visibility = View.GONE
                    goalsCompleted = 0
                }

                binding.txtTotalGoal.text = "${savings.size}"
                binding.txtTotalCompleted.text = "$goalsCompleted"
                binding.txtTotalInProgress.text = "${savings.size - goalsCompleted}"

            })
    }

    private var goalsCompleted: Int = 0
    private val countCompletedGoals: () -> Unit = {
        goalsCompleted++
        binding.txtTotalCompleted.text = "$goalsCompleted"
        binding.txtTotalInProgress.text = "${savings.size - goalsCompleted}"
    }

    override fun getLayoutId(): Int = R.layout.fragment_saving_box

}