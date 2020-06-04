package com.moony.calc.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.adapter.CategoriesListAdapter
import com.moony.calc.base.BaseActivity
import com.moony.calc.utils.AssetFolderManager
import kotlinx.android.synthetic.main.activity_add_categories.*
import kotlinx.android.synthetic.main.category_item.*


class AddCategoriesActivity : BaseActivity() {
    private var isIncome = true
    private var linkImage = ""

    companion object {
        val KEY = "com.moony.calc.activities.AddCategoriesActivity"
    }

    override fun init(savedInstanceState: Bundle?) {
        val intent = intent

        intent.let {
            isIncome = intent.getBooleanExtra(KEY, true)
        }

        if (isIncome) {
            toolbar_add_categories.title = resources.getString(R.string.add_income_category)
        } else {
            toolbar_add_categories.title = resources.getString(R.string.add_expense_category)
        }


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
}
