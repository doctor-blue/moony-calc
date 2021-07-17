package com.moony.calc.ui.category

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.devcomentry.moonlight.binding.BindingFragment
import com.moony.calc.R
import com.moony.calc.databinding.FragmentCategoriesBinding
import com.moony.calc.keys.MoonyKey
import com.moony.calc.model.Category
import com.moony.calc.ui.adapter.CategoryAdapter
import com.moony.calc.ui.transaction.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment(private val isIncome: Boolean, private val activity: AppCompatActivity) :
    BindingFragment<FragmentCategoriesBinding>(R.layout.fragment_categories) {

    private var categoryNames: ArrayList<String> = arrayListOf()

    private val transactionViewModel: TransactionViewModel by activityViewModels()

    private val categoryViewModel: CategoryViewModel by activityViewModels()

    private val categoryAdapter by lazy {
        CategoryAdapter(
            categoryViewModel,
            transactionViewModel,
            CategoriesActivity.isCanRemove,
            onCategoryItemClick
        )
    }


    override fun initControls(savedInstanceState: Bundle?) {
        binding {
            lifecycleOwner = viewLifecycleOwner
            isIncome = this@CategoriesFragment.isIncome
            adapter = categoryAdapter
            vm = categoryViewModel
        }

        categoryViewModel.getAllCategory(isIncome)
            .observe(viewLifecycleOwner, { list ->
                val categories = list as MutableList<Category>
                categoryNames = list.map { it.title } as ArrayList

                categories.add(Category("", "", isIncome, R.string.add))

                categoryViewModel.submitCategories(categories, isIncome)

            })
    }


    private val onCategoryItemClick: (Any) -> Unit = {
        val category: Category = it as Category
        if (category.resId == R.string.add) {
            val intent = Intent(activity, CategoryIconListActivity::class.java)
            intent.putExtra(CategoryIconListActivity.IS_INCOME_KEY, isIncome)
            intent.putExtra(CategoryIconListActivity.CATEGORY_NAMES, categoryNames)
            startActivity(intent)
        } else {
            if (!CategoriesActivity.isCanRemove) {
                val intent = Intent()
                intent.putExtra(MoonyKey.pickCategory, category)
                activity.setResult(Activity.RESULT_OK, intent)
                activity.finish()
            }
        }
    }
}