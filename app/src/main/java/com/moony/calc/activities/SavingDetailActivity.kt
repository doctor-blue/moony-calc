package com.moony.calc.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.moony.calc.R
import com.moony.calc.adapter.SavingDetailAdapter
import com.moony.calc.base.BaseActivity
import com.moony.calc.base.BaseFragment
import com.moony.calc.database.SavingHistoryViewModel
import com.moony.calc.database.SavingViewModel
import com.moony.calc.dialog.ConfirmDialogBuilder
import com.moony.calc.fragments.SavingBoxFragment
import com.moony.calc.fragments.SavingDetailFragment
import com.moony.calc.fragments.SavingHistoryFragment
import com.moony.calc.model.Saving
import kotlinx.android.synthetic.main.activity_saving_detail.*


class SavingDetailActivity : BaseActivity() {

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

    override fun init(savedInstanceState: Bundle?) {
        initControls(savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        toolbar_saving_detail.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initControls(savedInstanceState: Bundle?) {
        val intent: Intent = intent

        saving = intent.getSerializableExtra(SavingBoxFragment.SAVING_KEY) as Saving

        val fragments: List<BaseFragment> =
            mutableListOf(SavingDetailFragment(saving.idSaving), SavingHistoryFragment())
        val savingDetailAdapter = SavingDetailAdapter(supportFragmentManager, fragments, this)
        viewpager_detail_saving.adapter = savingDetailAdapter
        tab_layout_saving.setupWithViewPager(viewpager_detail_saving)


        setSupportActionBar(toolbar_saving_detail)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.saving_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mnu_edit_saving) {
            val intent = Intent(this, AddSavingGoalActivity::class.java)
            intent.putExtra(EDIT_SAVINGS, saving)
            startActivity(intent)
        } else {
            val builder = ConfirmDialogBuilder(this)
            builder.setContent(resources.getString(R.string.notice_delete_saving))
            val dialog = builder.createDialog()
            builder.btnConfirm.setOnClickListener {
                savingViewModel.deleteSaving(saving)
                savingHistoryViewModel.deleteAllSavingHistoryBySaving(saving.idSaving)
                dialog.dismiss()
                finish()
            }
            builder.btnCancel.setOnClickListener { dialog.dismiss() }
            builder.showDialog()
        }
        return true

    }
}