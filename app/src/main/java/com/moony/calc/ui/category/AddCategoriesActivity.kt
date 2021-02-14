package com.moony.calc.ui.category

import android.annotation.SuppressLint
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
import com.moony.calc.model.Category
import com.moony.calc.utils.AssetFolderManager
import kotlinx.android.synthetic.main.activity_add_categories.*


class AddCategoriesActivity : BaseActivity() {
    private var isIncome = true
    private var linkImage = ""

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }

    companion object {
       const val KEY = "com.moony.calc.ui.category.AddCategoriesActivity"
    }

    override fun init(savedInstanceState: Bundle?) {
        val intent = intent
        setSupportActionBar(toolbar_add_categories)

        intent.let {
            isIncome = intent.getBooleanExtra(KEY, true)
        }

        if (isIncome) {
            toolbar_add_categories.title = resources.getString(R.string.add_income_category)
        } else {
            toolbar_add_categories.title = resources.getString(R.string.add_expense_category)
        }
        toolbar_add_categories.setNavigationOnClickListener { finish() }


        with(AssetFolderManager) {
            context = this@AddCategoriesActivity
            addItemToMap()
        }

        val categoriesListAdapter =
            CategoriesListAdapter(AssetFolderManager.imageMap.keys.toList(), this) {
                linkImage = it.toString()
                Glide.with(this@AddCategoriesActivity)
                    .load(AssetFolderManager.assetPath + linkImage).into(img_choose_category)
            }
        rv_add_categories.setHasFixedSize(true)
        rv_add_categories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_add_categories.setItemViewCacheSize(20)
        rv_add_categories.adapter = categoriesListAdapter


    }

    override fun getLayoutId(): Int = R.layout.activity_add_categories

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.mnu_save == item.itemId) {
            saveCategory()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("WrongConstant")
    private fun saveCategory() {
        val snackBar: Snackbar =
            Snackbar.make(
                layout_root_add_categories,
                R.string.please_select_icon,
                Snackbar.LENGTH_LONG
            )
        snackBar.setTextColor(resources.getColor(R.color.white))
        snackBar.setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
        snackBar.animationMode = Snackbar.ANIMATION_MODE_FADE

        when {
            edt_title_category.text.toString().trim().isEmpty() -> {
                snackBar.setText(R.string.please_add_title)
                snackBar.show()
            }
            linkImage.isEmpty() -> {
                snackBar.show()
            }
            else -> {
                categoryViewModel.insertCategory(
                    Category(
                        edt_title_category.text.toString().trim(),
                        linkImage,
                        isIncome
                    )
                )
                finish()
            }
        }


    }
}
