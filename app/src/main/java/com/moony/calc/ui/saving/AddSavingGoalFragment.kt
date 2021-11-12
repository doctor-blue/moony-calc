package com.moony.calc.ui.saving

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.devcomentry.moonlight.binding.BindingFragment
import com.google.android.material.snackbar.Snackbar
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentAddSavingGoalBinding
import com.moony.calc.model.Saving
import com.moony.calc.ui.category.CategoryIconListActivity
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.setAutoHideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddSavingGoalFragment : BindingFragment<FragmentAddSavingGoalBinding>(R.layout.fragment_add_saving_goal) {

    private val calendar: Calendar = Calendar.getInstance()
    private var dueDate: Date? = null
    private val savingViewModel: SavingViewModel by activityViewModels()
    private var iconUrl = ""

    companion object {
        const val KEY_PICK_CATEGORY = 1101
        const val KEY_PICK_ICON = 1102
        const val ICON_LINK = "com.moony.calc.ui.saving.AddSavingGoalFragment.ICON_LINK"
        const val TITLE = "com.moony.calc.ui.saving.AddSavingGoalFragment.TITLE"
    }

    override fun initControls(savedInstanceState: Bundle?) {
        binding.toolbarAddSavingGoal.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.mnu_save) {
                saveSavingGoal()
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun saveSavingGoal() {
        val snackbar: Snackbar =
            Snackbar.make(
                binding.layoutRootAddGoal,
                R.string.empty_date_error,
                Snackbar.LENGTH_LONG
            )
        snackbar.setTextColor(resources.getColor(R.color.white))
        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        snackbar.animationMode = Snackbar.ANIMATION_MODE_SLIDE

        when {
            binding.edtGoalDescription.text.toString().trim().isEmpty() -> {
                binding.textInputGoalDescription.error = resources.getString(R.string.empty_error)
            }
            binding.edtGoalAmount.text.toString().trim().isEmpty() -> {
                binding.textInputGoalAmount.error = resources.getString(R.string.empty_error)
            }
            binding.txtDueDate.text.toString().trim().isEmpty() -> {
                hideKeyboard()
                snackbar.show()
            }
            iconUrl.isEmpty() -> {
                hideKeyboard()
                snackbar.setText(R.string.empty_icon_error)
                snackbar.show()
            }
            else -> {
                var title = binding.edtGoalDescription.text.toString().trim()
                title = title.replaceFirst(title[0], title[0].uppercaseChar())
                val saving = Saving(
                    title,
                    binding.edtGoalAmount.text.toString().trim().toDouble(),
                    dueDate!!,
                    "",
                    iconUrl
                )
                savingViewModel.insertSaving(saving)
                requireActivity().onBackPressed()
            }
        }
        hideKeyboard()
    }

    override fun initEvents() {

        binding.edtGoalAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                s?.let {
                    if (it.isNotEmpty()) {
                        binding.textInputGoalAmount.error = null
                        if (it.toString().contains('.') || it.toString().contains(',')) {
                            /**
                             * "${handleTextToDouble(it.toString())}0" nhiều trường hợp nó ở dạng #. thay vì #.# nên thêm 0 để đc chuỗi dạng #.0
                             * chuyển về kiểu double sau đó format nó sẽ được chuỗi ở dạng #.00 và lấy độ dài làm max length cho edit_text
                             */
                            val maxLength = "${handleTextToDouble(it.toString())}1".toDouble()
                                .decimalFormat().length
                            binding.edtGoalAmount.filters =
                                arrayOf(InputFilter.LengthFilter(maxLength))
                        } else {
                            binding.edtGoalAmount.filters = arrayOf(InputFilter.LengthFilter(13))
                            if (it.length - 1 == 12) {
                                //kiểm tra nếu kí tự cuối cùng không là . or , thì xóa kí tự đó đi
                                val lastChar = it[12]
                                if (!(lastChar == '.' || lastChar == ',')) {
                                    binding.edtGoalAmount.setText(it.subSequence(0, 12))
                                    binding.edtGoalAmount.setSelection(12)
                                }
                            }
                        }
                    }
                }
            }
        })

        binding.edtGoalDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtGoalDescription.text.toString().trim().isNotEmpty()) {
                    binding.textInputGoalDescription.error = null
                }
            }
        })

        binding.layoutGoalCalendar.setOnClickListener {
            pickDateTime()
        }

        binding.layoutGoalCategories.setOnClickListener {
            val intent = Intent(requireContext(), CategoryIconListActivity::class.java)
            intent.putExtra(CategoryIconListActivity.IS_PICK_ICON, true)
            startActivityForResult(
                intent,
                KEY_PICK_ICON
            )
        }

        binding.toolbarAddSavingGoal.setNavigationOnClickListener {
            requireActivity().onBackPressed()
            hideKeyboard()
        }

        binding.edtGoalAmount.setAutoHideKeyboard()
        binding.edtGoalDescription.setAutoHideKeyboard()
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
        val dialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

//            dueDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.time)
            dueDate = calendar.time
            binding.txtDueDate.text = resources.getString(R.string.due_date) + " " + SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(dueDate!!)
        }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == KEY_PICK_ICON)
            if (resultCode == Activity.RESULT_OK) {
                iconUrl = data?.getStringExtra(ICON_LINK) ?: ""
                val title = data?.getStringExtra(TITLE)

                Glide.with(this).load(AssetFolderManager.assetPath + iconUrl)
                    .into(binding.imgGoalCategory)
                binding.txtTitleCategoryAddSaving.text = title

            }
    }

    private fun hideKeyboard() {
        (requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            requireActivity().window.decorView.windowToken, 0
        )
    }
}