package com.moony.calc.ui.saving.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentSavingHistoryBinding
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingHistory

class SavingHistoryFragment() : BaseFragment() {
    private var saving:Saving? =null

    constructor(saving: Saving) : this() {
        this.saving = saving
    }

    private val savingHistoryViewModel: SavingHistoryViewModel by lazy {
        ViewModelProvider(requireActivity())[SavingHistoryViewModel::class.java]
    }

    override fun getLayoutId(): Int = R.layout.fragment_saving_history

    private val savingHistoryAdapter by lazy {
        SavingHistoryAdapter(onItemClick)
    }


    companion object {
        const val SAVING = "com.moony.calc.ui.saving.history.SavingHistoryFragment.SAVING"
        const val IS_SAVING = "com.moony.calc.ui.saving.history.SavingHistoryFragment.IS_SAVING"
        const val EDIT_HISTORY =
            "com.moony.calc.ui.saving.history.SavingHistoryFragment.EDIT_HISTORY"
    }
    private val binding: FragmentSavingHistoryBinding
        get() = (getViewBinding() as FragmentSavingHistoryBinding)
    

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
            val intent = Intent(baseContext, SavingHistoryActivity::class.java)
            intent.putExtra(SAVING, saving)
            intent.putExtra(IS_SAVING, true)
            startActivity(intent)
        }
        binding.btnSubtractSavingMoney.setOnClickListener {
            val intent = Intent(baseContext, SavingHistoryActivity::class.java)
            intent.putExtra(SAVING, saving)
            intent.putExtra(IS_SAVING, false)
            startActivity(intent)
        }

    }

    override fun initControls(view: View, savedInstanceState: Bundle?) {
        binding.rvSavingHistory.layoutManager = LinearLayoutManager(baseContext)
        binding.rvSavingHistory.setHasFixedSize(true)
        binding.rvSavingHistory.adapter = savingHistoryAdapter

        savingHistoryViewModel.getAllSavingHistory(saving!!.idSaving)
            .observe(viewLifecycleOwner, {
                savingHistoryAdapter.refreshData(it)
            })
    }

    private val onItemClick: (SavingHistory) -> Unit = {
        val intent = Intent(baseContext, SavingHistoryActivity::class.java)
        intent.putExtra(EDIT_HISTORY, it)
        intent.putExtra(SAVING, saving)
        startActivity(intent)
    }


}