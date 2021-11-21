package com.moony.calc.ui.saving.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.devcomentry.moonlight.binding.BindingFragment
import com.moony.calc.R
import com.moony.calc.databinding.FragmentSavingHistoryBinding
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingHistory
import com.moony.calc.ui.saving.history.adapter.SavingHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavingHistoryFragment() : BindingFragment<FragmentSavingHistoryBinding>(R.layout.fragment_saving_history) {
    private var saving: Saving? = null

    constructor(saving: Saving) : this() {
        this.saving = saving
    }

    private val savingHistoryViewModel: SavingHistoryViewModel by activityViewModels()


    private val savingHistoryAdapter by lazy {
        SavingHistoryAdapter(onItemClick)
    }


    companion object {
        const val SAVING = "com.moony.calc.ui.saving.history.SavingHistoryFragment.SAVING"
        const val IS_SAVING = "com.moony.calc.ui.saving.history.SavingHistoryFragment.IS_SAVING"
        const val EDIT_HISTORY =
                "com.moony.calc.ui.saving.history.SavingHistoryFragment.EDIT_HISTORY"
    }

    override fun initEvents() {
        binding.rvSavingHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.btnImportHistory.isShown) binding.btnImportHistory.visibility =
                        View.GONE
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) binding.btnImportHistory.visibility =
                        View.VISIBLE
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        binding.btnAddSavingMoney.setOnClickListener {
            val intent = Intent(requireContext(), SavingHistoryActivity::class.java)
            intent.putExtra(SAVING, saving)
            intent.putExtra(IS_SAVING, true)
            startActivity(intent)
        }
        binding.btnSubtractSavingMoney.setOnClickListener {
            val intent = Intent(requireContext(), SavingHistoryActivity::class.java)
            intent.putExtra(SAVING, saving)
            intent.putExtra(IS_SAVING, false)
            startActivity(intent)
        }

    }

    override fun initControls(savedInstanceState: Bundle?) {
        binding {
            lifecycleOwner = viewLifecycleOwner
            vm = savingHistoryViewModel
            adapter = savingHistoryAdapter
        }

        savingHistoryViewModel.getAllSavingHistory(saving!!.idSaving)
                .observe(viewLifecycleOwner, {
                    savingHistoryViewModel.submitList(it)
                })
    }

    private val onItemClick: (SavingHistory) -> Unit = {
        val intent = Intent(requireContext(), SavingHistoryDetailActivity::class.java)
        intent.putExtra(EDIT_HISTORY, it)
        intent.putExtra(SAVING, saving)
        startActivity(intent)
    }


}