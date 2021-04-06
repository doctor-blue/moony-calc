package com.moony.calc.ui.category

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.databinding.ActivityAddCategoriesBinding
import com.moony.calc.model.Category
import com.moony.calc.ui.saving.AddSavingGoalFragment
import com.moony.calc.utils.AssetFolderManager


class CategoryIconListActivity : BaseActivity() {
    private var isIncome = true
    private var iconUrl = ""
    private var isPickIcon = false

    private val binding: ActivityAddCategoriesBinding
        get() = (getViewBinding() as ActivityAddCategoriesBinding)

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }

    companion object {
        const val IS_INCOME_KEY = "com.moony.calc.ui.category.CategoryIconListActivity"
        const val IS_PICK_ICON = "com.moony.calc.ui.category.CategoryIconListActivity.PICK_ICON"
    }

    override fun initControls(savedInstanceState: Bundle?) {
        val intent = intent
        setSupportActionBar(binding.toolbarAddCategories)

        intent.let {
            isIncome = intent.getBooleanExtra(IS_INCOME_KEY, true)
            isPickIcon = intent.getBooleanExtra(IS_PICK_ICON, false)
        }
        if (!isPickIcon) {
            if (isIncome) {
                binding.toolbarAddCategories.title =
                    resources.getString(R.string.add_income_category)
            } else {
                binding.toolbarAddCategories.title =
                    resources.getString(R.string.add_expense_category)
            }
        } else {
            binding.edtTitleCategory.isFocusable = false
            binding.toolbarAddCategories.title =
                resources.getString(R.string.select_icon)
        }

        binding.toolbarAddCategories.setNavigationOnClickListener { finish() }


        with(AssetFolderManager) {
            context = this@CategoryIconListActivity
            addItemToMap()
        }

        val categoriesListAdapter =
            CategoriesListAdapter(
                AssetFolderManager.imageMap.keys.toList(),
                this
            ) { iconUrl, title ->
                this.iconUrl = iconUrl
                if (isPickIcon)
                    binding.edtTitleCategory.setText(title)
                Glide.with(this@CategoryIconListActivity)
                    .load(AssetFolderManager.assetPath + this.iconUrl)
                    .into(binding.imgChooseCategory)

            }
        binding.rvAddCategories.setHasFixedSize(true)
        binding.rvAddCategories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAddCategories.setItemViewCacheSize(20)
        binding.rvAddCategories.adapter = categoriesListAdapter


    }

    override fun getLayoutId(): Int = R.layout.activity_add_categories

    override fun initEvents() {

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
        val snackBar: Snackbar =
            Snackbar.make(
                binding.layoutRootAddCategories,
                R.string.please_select_icon,
                Snackbar.LENGTH_LONG
            )
        snackBar.setTextColor(resources.getColor(R.color.white))
        snackBar.setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
        snackBar.animationMode = Snackbar.ANIMATION_MODE_FADE

        when {
            binding.edtTitleCategory.text.toString().trim().isEmpty() -> {
                snackBar.setText(R.string.please_add_title)
                snackBar.show()
            }
            this.iconUrl.isEmpty() -> {
                snackBar.show()
            }
            else -> {
                if (isPickIcon) {
                    val intent = Intent()
                    intent.putExtra(AddSavingGoalFragment.ICON_LINK, this.iconUrl)
                    intent.putExtra(
                        AddSavingGoalFragment.TITLE,
                        binding.edtTitleCategory.text.toString().trim()
                    )
                    setResult(Activity.RESULT_OK, intent)
                } else {
                    categoryViewModel.insertCategory(
                        Category(
                            binding.edtTitleCategory.text.toString().trim(),
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
