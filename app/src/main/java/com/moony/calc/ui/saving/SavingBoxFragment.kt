package com.moony.calc.ui.saving

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentSavingBoxBinding
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingItem
import com.moony.calc.ui.adapter.SavingBoxAdapter

class SavingBoxFragment : BaseFragment() {
    private lateinit var savings: List<Saving>
    private val savingViewModel: SavingViewModel by lazy { ViewModelProvider(this)[SavingViewModel::class.java] }
    private val adapter: SavingBoxAdapter by lazy {
        SavingBoxAdapter(requireActivity()) {
            val saving: Saving = it as Saving
            val intent = Intent(baseContext, SavingDetailActivity::class.java)
            intent.putExtra(SAVING_KEY, saving)
            startActivity(intent)
        }
    }

    private var savingItemsLiveData: LiveData<List<SavingItem>>? = null

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

        savingItemsLiveData = savingViewModel.getAllSavingItem()

        savingViewModel.getAllSaving().observe(this,
            { savings ->
                this.savings = savings
                adapter.setSavings(savings)

                savingItemsLiveData?.removeObserver(updateCountObserver)
                savingItemsLiveData?.observe(viewLifecycleOwner, updateCountObserver)

                if (savings.isNotEmpty()) {
                    binding.layoutListEmpty.visibility = View.GONE
                    binding.rvSavingBox.visibility = View.VISIBLE
                } else {
                    binding.layoutListEmpty.visibility = View.VISIBLE
                    binding.rvSavingBox.visibility = View.GONE
                }


            })
    }

    private val updateCountObserver = Observer<List<SavingItem>> { items ->
        val completedList = items.filter { it.sum >= it.saving.desiredAmount }
        binding.txtTotalGoal.text = "${savings.size}"
        binding.txtTotalCompleted.text = "${completedList.size}"
        binding.txtTotalInProgress.text = "${savings.size - completedList.size}"
    }

    override fun getLayoutId(): Int = R.layout.fragment_saving_box

}