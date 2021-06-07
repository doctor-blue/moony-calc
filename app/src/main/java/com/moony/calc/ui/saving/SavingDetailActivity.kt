package com.moony.calc.ui.saving

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.ActivitySavingDetailBinding
import com.moony.calc.model.Saving
import com.moony.calc.ui.dialog.ConfirmDialogBuilder
import com.moony.calc.ui.saving.history.SavingHistoryFragment
import com.moony.calc.ui.saving.history.SavingHistoryViewModel


class SavingDetailActivity : BaseActivity() {
    private val binding: ActivitySavingDetailBinding
        get() = (getViewBinding() as ActivitySavingDetailBinding)

    override fun getLayoutId(): Int = R.layout.activity_saving_detail

    companion object {
        const val EDIT_SAVINGS = "com.moony.calc.activities.EDIT_SAVINGS"
    }

    private lateinit var saving: Saving
    private val savingViewModel: SavingViewModel by lazy {
        ViewModelProvider(this)[SavingViewModel::class.java]
    }
    private val savingHistoryViewModel: SavingHistoryViewModel by lazy {
        ViewModelProvider(this)[SavingHistoryViewModel::class.java]
    }

    override fun initEvents() {
        binding.toolbarSavingDetail.setNavigationOnClickListener {
            finish()
        }
    }

    override fun initControls(savedInstanceState: Bundle?) {
        val intent: Intent = intent

        saving = intent.getSerializableExtra(SavingBoxFragment.SAVING_KEY) as Saving

        val fragments: List<BaseFragment> =
            mutableListOf(
                SavingDetailFragment(saving),
                SavingHistoryFragment(saving)
            )
        val savingDetailAdapter = SavingDetailAdapter(supportFragmentManager, fragments, this)
        binding.viewpagerDetailSaving.adapter = savingDetailAdapter
        binding.tabLayoutSaving.setupWithViewPager(binding.viewpagerDetailSaving)
        binding.toolbarSavingDetail.title = saving.title


        setSupportActionBar(binding.toolbarSavingDetail)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.saving_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mnu_edit_saving) {
            val intent = Intent(this, UpdateSavingGoalActivity::class.java)
            intent.putExtra(EDIT_SAVINGS, saving)
            startActivity(intent)
        } else {
            val builder = ConfirmDialogBuilder(this)
            builder.setContent(resources.getString(R.string.notice_delete_saving))
            val dialog = builder.createDialog()
            builder.btnConfirm.setOnClickListener {
                savingHistoryViewModel.deleteAllTransactionBySaving(saving.idSaving)
                savingViewModel.deleteSaving(saving)
                dialog.dismiss()
                finish()
            }
            builder.btnCancel.setOnClickListener { dialog.dismiss() }
            builder.showDialog()
        }
        return true

    }
}