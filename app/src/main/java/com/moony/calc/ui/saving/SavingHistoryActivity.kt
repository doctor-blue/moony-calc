package com.moony.calc.ui.saving

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.keys.MoonyKey
import com.moony.calc.model.Category
import com.moony.calc.model.SavingHistory
import com.moony.calc.model.SavingHistoryItem
import com.moony.calc.ui.category.CategoriesActivity
import com.moony.calc.ui.saving.history.SavingHistoryFragment
import com.moony.calc.ui.saving.history.SavingHistoryViewModel
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.decimalFormat
import kotlinx.android.synthetic.main.activity_saving_history.*
import java.text.SimpleDateFormat
import java.util.*

class SavingHistoryActivity : BaseActivity() {

    private var calendar = Calendar.getInstance()
    private var dateAdded = ""
    private var isSaving = true
    private var idSaving = -1
    private var savingHistoryItem: SavingHistoryItem? = null
    private var category: Category? = null

    private val savingHistoryViewModel: SavingHistoryViewModel by lazy {
        ViewModelProvider(this)[SavingHistoryViewModel::class.java]
    }

    override fun getLayoutId(): Int = R.layout.activity_saving_history


    override fun init(savedInstanceState: Bundle?) {
        initControls()
        initEvents()
    }

    private fun initControls() {
        idSaving = intent.getIntExtra(SavingHistoryFragment.ID_SAVING, -1)
        isSaving = intent.getBooleanExtra(SavingHistoryFragment.IS_SAVING, true)
        savingHistoryItem =
            intent.getSerializableExtra(SavingHistoryFragment.EDIT_HISTORY) as SavingHistoryItem?

        savingHistoryItem?.let {
            isSaving = it.history.isSaving

            edt_history_saving_description.setText(it.history.description)

            edt_saving_history_amount.setText((it.history.amount.toString()))
            dateAdded = it.history.date
            txt_due_date.text = (resources.getString(R.string.date) + " " + dateAdded)

            txt_title_category_saving_history.text = it.category.title
            Glide.with(this).load(AssetFolderManager.assetPath + it.category.iconUrl)
                .into(img_saving_history_category)
        }

        if (isSaving)
            toolbar_history_saving.title = resources.getString(R.string.more_money_on)
        else
            toolbar_history_saving.title = resources.getString(R.string.get_money_out)




        setSupportActionBar(toolbar_history_saving)
    }

    private fun initEvents() {
        edt_saving_history_amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                s?.let {
                    if (it.isNotEmpty()) {
                        tip_history_saving_amount.error = null
                        if (it.toString().contains('.') || it.toString().contains(',')) {
                            /**
                             * "${handleTextToDouble(it.toString())}0" nhiều trường hợp nó ở dạng #. thay vì #.# nên thêm 0 để đc chuỗi dạng #.0
                             * chuyển về kiểu double sau đó format nó sẽ được chuỗi ở dạng #.00 và lấy độ dài làm max length cho edit_text
                             */
                            val maxLength = "${handleTextToDouble(it.toString())}1".toDouble()
                                .decimalFormat().length
                            edt_saving_history_amount.filters =
                                arrayOf(InputFilter.LengthFilter(maxLength))
                        } else {
                            edt_saving_history_amount.filters = arrayOf(InputFilter.LengthFilter(9))
                            if (it.length - 1 == 8) {
                                //kiểm tra nếu kí tự cuối cùng không là . or , thì xóa kí tự đó đi
                                val lastChar = it[8]
                                if (!(lastChar == '.' || lastChar == ',')) {
                                    edt_saving_history_amount.setText(it.subSequence(0, 8))
                                    edt_saving_history_amount.setSelection(8)
                                }
                            }
                        }
                    }
                }
            }
        })

        edt_history_saving_description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edt_history_saving_description.text.toString().trim().isNotEmpty()) {
                    tip_history_saving_description.error = null
                }
            }
        })

        layout_saving_history_calendar.setOnClickListener {
            pickDateTime()
        }
        layout_saving_history_categories.setOnClickListener {
            val intent: Intent = Intent(this@SavingHistoryActivity, CategoriesActivity::class.java)
            startActivityForResult(intent, AddSavingGoalFragment.KEY_PICK_CATEGORY)
        }
        toolbar_history_saving.setNavigationOnClickListener { finish() }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mnu_save) {
            saveHistory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveHistory() {
        val snackbar: Snackbar =
            Snackbar.make(
                layout_root_saving_history,
                R.string.empty_date_error,
                Snackbar.LENGTH_LONG
            )
        snackbar.setTextColor(resources.getColor(R.color.white))
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
        snackbar.animationMode = Snackbar.ANIMATION_MODE_SLIDE


        when {
            edt_saving_history_amount.text.toString().trim().isEmpty() -> {
                tip_history_saving_amount.error = resources.getString(R.string.empty_error)
            }
            edt_history_saving_description.text.toString().trim().isEmpty() -> {
                tip_history_saving_description.error = resources.getString(R.string.empty_error)
            }
            txt_due_date.text.toString().trim().isEmpty() -> {
                snackbar.show()
            }
            txt_title_category_saving_history.text.toString().trim().isEmpty() -> {
                snackbar.setText(R.string.empty_category_error)
                snackbar.show()
            }
            else -> {
                if (savingHistoryItem != null) {
                    var amount = edt_saving_history_amount.text.toString().toDouble()
                    if (!isSaving) amount *= -1

                    savingHistoryItem!!.history.amount = amount
                    savingHistoryItem!!.history.isSaving = isSaving
                    savingHistoryItem!!.history.date = dateAdded
                    savingHistoryItem!!.history.description =
                        edt_history_saving_description.text.toString()

                    savingHistoryViewModel.updateSavingHistory(savingHistoryItem!!.history)
                    finish()

                } else {
                    var amount = edt_saving_history_amount.text.toString().toDouble()
                    if (!isSaving) amount *= -1

                    category?.let {
                        val savingHistory = SavingHistory(
                            edt_history_saving_description.text.toString(),
                            idSaving,
                            amount,
                            isSaving,
                            it.idCategory,
                            dateAdded
                        )
                        savingHistoryViewModel.insertSavingHistory(savingHistory)
                        finish()
                    }
                }
            }
        }
    }

    private fun handleTextToDouble(s: String): String {
        var text = s
        if (text.contains(',')) {
            text = text.replace(',', '.')
        }
        return text
    }

    @SuppressLint("SetTextI18n")
    private fun pickDateTime() {
        val dialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            dateAdded = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.time)
            txt_due_date.text = resources.getString(R.string.date) + " " + dateAdded
        }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])
        dialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddSavingGoalFragment.KEY_PICK_CATEGORY)
            if (resultCode == Activity.RESULT_OK) {
                category = data?.getSerializableExtra(MoonyKey.pickCategory) as Category
                savingHistoryItem?.let {
                    it.category = category!!
                }
                Glide.with(this).load(AssetFolderManager.assetPath + category!!.iconUrl)
                    .into(img_saving_history_category)

                txt_title_category_saving_history.text = category!!.title


            }
    }
}