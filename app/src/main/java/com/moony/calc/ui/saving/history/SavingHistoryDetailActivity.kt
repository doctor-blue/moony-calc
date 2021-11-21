package com.moony.calc.ui.saving.history

import android.content.Intent
import android.os.Bundle
import com.devcomentry.moonlight.binding.BindingActivity
import com.moony.calc.R
import com.moony.calc.databinding.FragmentSavingHistoryDetailBinding
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingHistory
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.setPreventDoubleClick

class SavingHistoryDetailActivity :
    BindingActivity<FragmentSavingHistoryDetailBinding>(R.layout.fragment_saving_history_detail) {
    private var saving: Saving? = null
    private var savingHistory: SavingHistory? = null

    override fun initControls(savedInstanceState: Bundle?) {
        super.initControls(savedInstanceState)
        saving = intent.getSerializableExtra(SavingHistoryFragment.SAVING) as Saving
        savingHistory =
            intent.getSerializableExtra(SavingHistoryFragment.EDIT_HISTORY) as SavingHistory?

        savingHistory?.let { savingHistory ->
            binding {
                txtHistoryMoney.text = savingHistory.amount.decimalFormat()
                if (savingHistory.amount > 0) {
                    txtHistoryTitle.text = resources.getString(R.string.money_in)
                    imgHistory.setImageResource(R.drawable.ic_money_in)
                } else {
                    txtHistoryTitle.text = resources.getString(R.string.money_out)
                    imgHistory.setImageResource(R.drawable.ic_money_out)

                }

                txtHistoryNote.text = savingHistory.description
                txtHistoryDate.text = savingHistory.date
            }
        }
    }

    override fun initEvents() {
        super.initEvents()
        binding {
            btnUpdateHistory.setPreventDoubleClick {
                updateSavingHistory()
            }
        }
    }
    companion object{
        lateinit var thisActivity: SavingHistoryDetailActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisActivity = this
    }

    private fun updateSavingHistory() {
        val intent = Intent(this, SavingHistoryActivity::class.java)
        intent.putExtra(SavingHistoryFragment.EDIT_HISTORY, savingHistory)
        intent.putExtra(SavingHistoryFragment.SAVING, saving)
        startActivity(intent)
    }
}