package com.moony.calc.ui.category

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentCategoriesBinding
import com.moony.calc.keys.MoonyKey
import com.moony.calc.model.Category

class CategoriesFragment(private val isIncome: Boolean, private val activity: BaseActivity) :
    BaseFragment() {

    private var categoryNames: ArrayList<String> = arrayListOf()

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(fragmentActivity!!)[CategoryViewModel::class.java]
    }

    private val binding: FragmentCategoriesBinding
        get() = (getViewBinding() as FragmentCategoriesBinding)

    override fun initControls(view: View, savedInstanceState: Bundle?) {
        categoryViewModel.getAllCategory(isIncome)
            .observe(viewLifecycleOwner, { list ->
                val categories = list as MutableList<Category>
                categoryNames = list.map { it.title } as ArrayList

                categories.add(Category(activity.resources.getString(R.string.add), "", isIncome))

                val categoryAdapter = CategoryAdapter(
                    activity, null, categories,
                    CategoriesActivity.isJustWatch
                ) {
                    val category: Category = it as Category
                    if (category.title == activity.resources.getString(R.string.add)) {
                        val intent = Intent(activity, CategoryIconListActivity::class.java)
                        intent.putExtra(CategoryIconListActivity.IS_INCOME_KEY, isIncome)
                        intent.putExtra(CategoryIconListActivity.CATEGORY_NAMES, categoryNames)
                        startActivity(intent)
                    } else {
                        if (!CategoriesActivity.isJustWatch) {
                            val intent = Intent()
                            intent.putExtra(MoonyKey.pickCategory, category)
                            activity.setResult(Activity.RESULT_OK, intent)
                            activity.finish()
                        }
                    }
                }
                binding.rvCategories.setHasFixedSize(true)
                binding.rvCategories.layoutManager = GridLayoutManager(activity, 4)
                binding.rvCategories.adapter = categoryAdapter
            })
    }

    override fun initEvents() {

    }

    override fun getLayoutId(): Int = R.layout.fragment_categories
}