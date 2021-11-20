package com.moony.calc.ui.saving

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.devcomentry.moonlight.binding.BindingActivity
import com.moony.calc.R
import com.moony.calc.databinding.ActivitySavingDetailBinding
import com.moony.calc.model.Saving
import com.moony.calc.ui.saving.adapter.SavingDetailAdapter
import com.moony.calc.ui.saving.history.SavingHistoryFragment
import com.moony.calc.ui.saving.history.SavingHistoryViewModel
import com.moony.calc.utils.showDialogDelete
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavingDetailActivity :
    BindingActivity<ActivitySavingDetailBinding>(R.layout.activity_saving_detail) {

    companion object {
        const val EDIT_SAVINGS = "com.moony.calc.activities.EDIT_SAVINGS"
    }

    private lateinit var saving: Saving
    private val savingViewModel: SavingViewModel by viewModels()

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

        val fragments: List<Fragment> =
            mutableListOf(
                SavingDetailFragment(saving),
                SavingHistoryFragment(saving)
            )
        val savingDetailAdapter = SavingDetailAdapter(supportFragmentManager, fragments, this)
        binding {
            viewpagerDetailSaving.adapter = savingDetailAdapter
            tabLayoutSaving.setupWithViewPager(viewpagerDetailSaving)
            toolbarSavingDetail.title = saving.title
            setSupportActionBar(toolbarSavingDetail)

        }


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
            showDialogDelete(R.string.notice_delete_saving) {
                savingHistoryViewModel.deleteAllTransactionBySaving(saving.idSaving)
                savingViewModel.deleteSaving(saving)
                finish()
            }
        }
        return true

    }
}