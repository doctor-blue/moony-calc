package com.moony.calc.ui.category

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devcomentry.moonlight.binding.BindingActivity
import com.google.android.material.snackbar.Snackbar
import com.moony.calc.R
import com.moony.calc.databinding.ActivityAddCategoriesBinding
import com.moony.calc.model.Category
import com.moony.calc.ui.adapter.CategoriesListAdapter
import com.moony.calc.ui.saving.AddSavingGoalFragment
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.setAutoHideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryIconListActivity :
    BindingActivity<ActivityAddCategoriesBinding>(R.layout.activity_add_categories) {
    private var isIncome = true
    private var iconUrl = ""
    private var isPickIcon = false
    private var categoryNames: ArrayList<String>? = arrayListOf()

    private val categoryViewModel: CategoryViewModel by viewModels()

    companion object {
        const val IS_INCOME_KEY = "com.moony.calc.ui.category.CategoryIconListActivity"
        const val IS_PICK_ICON = "com.moony.calc.ui.category.CategoryIconListActivity.PICK_ICON"
        const val CATEGORY_NAMES =
            "com.moony.calc.ui.category.CategoryIconListActivity.CATEGORY_NAMES"
    }

    override fun initControls(savedInstanceState: Bundle?) {
        val intent = intent
        binding {
            setSupportActionBar(toolbarAddCategories)

            intent.let {
                isIncome = intent.getBooleanExtra(IS_INCOME_KEY, true)
                isPickIcon = intent.getBooleanExtra(IS_PICK_ICON, false)
                categoryNames = intent.getStringArrayListExtra(CATEGORY_NAMES)
            }
            if (!isPickIcon) {
                if (isIncome) {
                    toolbarAddCategories.title =
                        resources.getString(R.string.add_income_category)
                } else {
                    toolbarAddCategories.title =
                        resources.getString(R.string.add_expense_category)
                }
            } else {
                edtTitleCategory.isFocusable = false
                toolbarAddCategories.title =
                    resources.getString(R.string.select_icon)
            }

            toolbarAddCategories.setNavigationOnClickListener { finish() }


            with(AssetFolderManager) {
                context = this@CategoryIconListActivity
                addItemToMap()
            }

            val categoriesListAdapter =
                CategoriesListAdapter(
                    AssetFolderManager.imageMap.keys.toList(),
                    this@CategoryIconListActivity
                ) { iconUrl, title ->
                    this@CategoryIconListActivity.iconUrl = iconUrl
                    if (isPickIcon)
                        edtTitleCategory.setText(title)
                    Glide.with(this@CategoryIconListActivity)
                        .load(AssetFolderManager.assetPath + this@CategoryIconListActivity.iconUrl)
                        .into(imgChooseCategory)

                }
            rvAddCategories.setHasFixedSize(true)
            rvAddCategories.layoutManager =
                LinearLayoutManager(
                    this@CategoryIconListActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            rvAddCategories.setItemViewCacheSize(20)
            rvAddCategories.adapter = categoriesListAdapter

        }
    }


    override fun initEvents() {
        binding.edtTitleCategory.setAutoHideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.mnu_save == item.itemId) {
            save()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("WrongConstant")
    private fun save() {
        if (categoryNames == null) categoryNames = arrayListOf("")
        val snackBar: Snackbar =
            Snackbar.make(
                binding.layoutRootAddCategories,
                R.string.please_select_icon,
                Snackbar.LENGTH_LONG
            )
        snackBar.setTextColor(resources.getColor(R.color.white))
        snackBar.setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
        snackBar.animationMode = Snackbar.ANIMATION_MODE_FADE
        var title = binding.edtTitleCategory.text.toString().trim()
        if (title.isNotEmpty())
            title = title.replaceFirst(title[0], title[0].uppercaseChar())

        when {
            title.isEmpty() -> {
                snackBar.setText(R.string.please_add_title)
                snackBar.show()
            }
            this.iconUrl.isEmpty() -> {
                snackBar.show()
            }

            categoryNames!!.contains(title) -> {
                snackBar.setText(R.string.exist_name_error)
                snackBar.show()
            }

            else -> {
                if (isPickIcon) {
                    val intent = Intent()
                    intent.putExtra(AddSavingGoalFragment.ICON_LINK, this.iconUrl)
                    intent.putExtra(
                        AddSavingGoalFragment.TITLE,
                        title
                    )
                    setResult(Activity.RESULT_OK, intent)
                } else {
                    categoryViewModel.insertCategory(
                        Category(
                            title,
                            this.iconUrl,
                            isIncome
                        )
                    )
                }
                finish()
            }
        }


    }
}
