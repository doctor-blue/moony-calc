package com.moony.calc.ui.saving.history

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.devcomentry.moonlight.binding.BindingActivity
import com.moony.calc.R
import com.moony.calc.databinding.FragmentSavingHistoryDetailBinding
import com.moony.calc.model.Saving
import com.moony.calc.model.SavingHistory
import com.moony.calc.model.Transaction
import com.moony.calc.ui.transaction.TransactionViewModel
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.setPreventDoubleClick
import com.moony.calc.utils.showDialogDelete
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.abs

@AndroidEntryPoint
class SavingHistoryDetailActivity :
    BindingActivity<FragmentSavingHistoryDetailBinding>(R.layout.fragment_saving_history_detail) {
    private var saving: Saving? = null
    private var savingHistory: SavingHistory? = null
    private val historyViewModel :SavingHistoryViewModel by viewModels()
    private val transactionViewModel :TransactionViewModel by viewModels()

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
            toolbarHistoryDetail.setOnMenuItemClickListener {
                if (it.itemId == R.id.mnu_delete) {
                    this@SavingHistoryDetailActivity.showDialogDelete(
                        R.string.confirm_delete_mess
                    ) {
                        val transaction = Transaction(
                            0.0,
                            "",
                            "",
                            Date()
                        )
                        transaction.idTransaction = savingHistory!!.idTransaction
                        historyViewModel.deleteSavingHistoryByTransaction(savingHistory!!.idTransaction)
                        transactionViewModel.deleteTransaction(transaction)
                        finish()
                    }
                }
                true
            }
        }
    }

    companion object {
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