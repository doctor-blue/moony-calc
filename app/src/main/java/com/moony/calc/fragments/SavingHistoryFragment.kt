package com.moony.calc.fragments

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moony.calc.R
import com.moony.calc.activities.SavingHistoryActivity
import com.moony.calc.adapter.SavingHistoryAdapter
import com.moony.calc.base.BaseFragment
import com.moony.calc.database.SavingHistoryViewModel
import kotlinx.android.synthetic.main.fragment_saving_history.*

class SavingHistoryFragment() : BaseFragment() {
    private var idSaving:Int=0
    constructor(idSaving:Int):this(){
        this.idSaving=idSaving
    }
    private val savingHistoryViewModel: SavingHistoryViewModel by lazy {
        ViewModelProvider(requireActivity())[SavingHistoryViewModel::class.java]
    }

    override fun getLayoutId(): Int = R.layout.fragment_saving_history

    companion object {
        const val ID_SAVING = "com.moony.calc.fragments.SavingHistoryFragment.ID_SAVING"
        const val IS_SAVING="com.moony.calc.fragments.SavingHistoryFragment.IS_SAVING"
        const val EDIT_HISTORY="com.moony.calc.fragments.SavingHistoryFragment.EDIT_HISTORY"
    }

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

        btn_add_saving_money.setOnClickListener {
            val intent = Intent(baseContext, SavingHistoryActivity::class.java)
            intent.putExtra(ID_SAVING, idSaving)
            intent.putExtra(IS_SAVING,true)
            startActivity(intent)
        }
        btn_subtract_saving_money.setOnClickListener {
            val intent = Intent(baseContext, SavingHistoryActivity::class.java)
            intent.putExtra(ID_SAVING, idSaving)
            intent.putExtra(IS_SAVING,false)
            startActivity(intent)
        }

    }

    private fun initControls() {
        rv_saving_history.layoutManager = LinearLayoutManager(baseContext)
        rv_saving_history.setHasFixedSize(true)

        savingHistoryViewModel.getAllSavingHistory(idSaving).observe(viewLifecycleOwner, Observer {
            val savingHistoryAdapter = SavingHistoryAdapter(requireActivity(), it) {history->
                val intent=Intent(baseContext,SavingHistoryActivity::class.java)
                intent.putExtra(EDIT_HISTORY,history)
                startActivity(intent)
            }
            rv_saving_history.adapter = savingHistoryAdapter


        })
    }


}