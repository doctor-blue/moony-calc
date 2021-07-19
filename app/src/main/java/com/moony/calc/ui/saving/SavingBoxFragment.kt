package com.moony.calc.ui.saving

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devcomentry.moonlight.binding.BindingActivity
import com.devcomentry.moonlight.binding.BindingFragment
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentSavingBoxBinding
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingItem
import com.moony.calc.ui.adapter.SavingBoxAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavingBoxFragment : BindingFragment<FragmentSavingBoxBinding>(R.layout.fragment_saving_box) {
    private lateinit var savings: List<Saving>
    private val savingViewModel: SavingViewModel by activityViewModels()

    private val adapter: SavingBoxAdapter by lazy {
        SavingBoxAdapter(requireActivity()) {
            val saving: Saving = it as Saving
            val intent = Intent(requireContext(), SavingDetailActivity::class.java)
            intent.putExtra(SAVING_KEY, saving)
            startActivity(intent)
        }
    }

    private var savingItemsLiveData: LiveData<List<SavingItem>>? = null

    companion object {
        const val SAVING_KEY = "com.moony.calc.ui.saving.SavingBoxFragment.SAVING_KEY"
    }

    override fun initEvents() {
        binding.btnAddSavingGoals.setOnClickListener {
            findNavController().navigate(R.id.action_saving_fragment_to_addSavingGoalActivity)
        }
    }

    override fun initControls( savedInstanceState: Bundle?) {
        binding {
            lifecycleOwner = viewLifecycleOwner
            vm = savingViewModel
            adapter = this@SavingBoxFragment.adapter

            savingItemsLiveData = savingViewModel.getAllSavingItem()

            savingViewModel.getAllSaving().observe(this@SavingBoxFragment,
                    { savings ->
                        this@SavingBoxFragment.savings = savings
                        savingViewModel.submitList(savings)

                        savingItemsLiveData?.removeObserver(updateCountObserver)
                        savingItemsLiveData?.observe(viewLifecycleOwner, updateCountObserver)

//                        if (savings.isNotEmpty()) {
//                            layoutListEmpty.visibility = View.GONE
//                            rvSavingBox.visibility = View.VISIBLE
//                        } else {
//                            layoutListEmpty.visibility = View.VISIBLE
//                            rvSavingBox.visibility = View.GONE
//                        }


                    })
        }
    }

    private val updateCountObserver = Observer<List<SavingItem>> { items ->
        val completedList = items.filter { it.sum >= it.saving.desiredAmount }
       binding {
           txtTotalGoal.text = "${savings.size}"
           txtTotalCompleted.text = "${completedList.size}"
           txtTotalInProgress.text = "${savings.size - completedList.size}"
       }
    }

}