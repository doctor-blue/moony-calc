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

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(fragmentActivity!!)[CategoryViewModel::class.java]
    }

    private val binding: FragmentCategoriesBinding
        get() = (getViewBinding() as FragmentCategoriesBinding)

    override fun initControls(view: View, savedInstanceState: Bundle?) {
        categoryViewModel.getAllCategory(isIncome)
            .observe(viewLifecycleOwner, { list ->
                val categories = list as MutableList<Category>

                categories.add(Category(activity.resources.getString(R.string.add), "", isIncome))

                val categoryAdapter = CategoryAdapter(
                    activity, null, categories,
                    CategoriesActivity.isJustWatch
                ) {
                    val category: Category = it as Category
                    if (category.title == activity.resources.getString(R.string.add)) {
                        val intent = Intent(activity, AddCategoriesActivity::class.java)
                        intent.putExtra(AddCategoriesActivity.KEY, isIncome)
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