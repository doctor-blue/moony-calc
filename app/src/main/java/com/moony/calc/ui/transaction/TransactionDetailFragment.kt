package com.moony.calc.ui.transaction

import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.ui.category.CategoryViewModel
import com.moony.calc.ui.dialog.ConfirmDialogBuilder
import com.moony.calc.model.Category
import com.moony.calc.model.Transaction
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.formatDateTime
import kotlinx.android.synthetic.main.fragment_transaction_detail.*
import java.util.*

class TransactionDetailFragment : BaseFragment() {
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }
    private val transactionViewModel: TransactionViewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }
    private lateinit var transaction: Transaction
    private lateinit var category: Category
    private var calendar = Calendar.getInstance()

    private val settings: Settings by lazy {
        Settings.getInstance(baseContext!!)
    }

    override fun init() {
        initControls()
        initEvents()
    }

    private fun initControls() {
        transaction = arguments?.getSerializable(getString(R.string.transaction)) as Transaction

        Log.d("TransactionDetail", transaction.idCategory.toString())

        txt_transaction_note.text = transaction.note

        categoryViewModel.getCategory(transaction.idCategory).observe(viewLifecycleOwner, Observer {
            Glide.with(this).load(AssetFolderManager.assetPath + it.iconUrl)
                .into(img_category)
            txt_category_title.text = it.title

            category = it
            if (!it.isIncome) {
                txt_transaction_money.text =
                    ((-1 * transaction.money).decimalFormat() + settings.getString(
                        Settings.SettingKey.CURRENCY_UNIT
                    ))
            } else
                txt_transaction_money.text =
                    (transaction.money.decimalFormat() + settings.getString(
                        Settings.SettingKey.CURRENCY_UNIT
                    ))

        })

        calendar.set(Calendar.DAY_OF_MONTH, transaction.day)
        calendar.set(Calendar.MONTH, transaction.month)
        calendar.set(Calendar.YEAR, transaction.year)
        txt_transaction_date.text = calendar.formatDateTime()

        calendar.set(Calendar.DAY_OF_MONTH, transaction.day)
    }


    private fun initEvents() {
        btn_update_transaction.setOnClickListener {
            val bundle = bundleOf(
                getString(R.string.transaction) to transaction,
                getString(R.string.categories) to category
            )
            findNavController().navigate(
                R.id.action_transactionDetailFragment_to_updateTransactionFragment,
                bundle
            )
        }
        toolbar_transaction_detail.setOnMenuItemClickListener {
            if (it.itemId == R.id.mnu_delete) {
                val builder = ConfirmDialogBuilder(requireContext())
                builder.setContent(resources.getString(R.string.notice_delete_transaction))
                val dialog = builder.createDialog()
                builder.btnConfirm.setOnClickListener {
                    transactionViewModel.deleteTransaction(transaction)
                    dialog.dismiss()
                    requireActivity().onBackPressed()
                }
                builder.btnCancel.setOnClickListener { dialog.dismiss() }
                builder.showDialog()
            }
            true
        }
        toolbar_transaction_detail.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_transaction_detail

}