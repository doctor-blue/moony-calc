package com.moony.calc.ui.transaction

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.model.TransactionItem
import com.moony.calc.ui.dialog.ConfirmDialogBuilder
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.formatDateTime
import kotlinx.android.synthetic.main.fragment_transaction_detail.*
import java.util.*

class TransactionDetailFragment : BaseFragment() {

    private val transactionViewModel: TransactionViewModel by lazy {
        ViewModelProvider(this)[TransactionViewModel::class.java]
    }
    private lateinit var transactionItem: TransactionItem
    private var calendar = Calendar.getInstance()

    private val settings: Settings by lazy {
        Settings.getInstance(baseContext!!)
    }

    override fun init() {
        initControls()
        initEvents()
    }

    private fun initControls() {
        transactionItem =
            arguments?.getSerializable(getString(R.string.transaction)) as TransactionItem

//        Log.d("TransactionDetail", transaction.idCategory.toString())

        txt_transaction_note.text = transactionItem.transaction.note

        Glide.with(this).load(AssetFolderManager.assetPath + transactionItem.category.iconUrl)
            .into(img_category)
        txt_category_title.text = transactionItem.category.title

        if (!transactionItem.category.isIncome) {
            txt_transaction_money.text =
                ((-1 * transactionItem.transaction.money).decimalFormat() + settings.getString(
                    Settings.SettingKey.CURRENCY_UNIT
                ))
        } else
            txt_transaction_money.text =
                (transactionItem.transaction.money.decimalFormat() + settings.getString(
                    Settings.SettingKey.CURRENCY_UNIT
                ))

        calendar.set(Calendar.DAY_OF_MONTH, transactionItem.transaction.day)
        calendar.set(Calendar.MONTH, transactionItem.transaction.month)
        calendar.set(Calendar.YEAR, transactionItem.transaction.year)
        txt_transaction_date.text = calendar.formatDateTime()

        calendar.set(Calendar.DAY_OF_MONTH, transactionItem.transaction.day)
    }


    private fun initEvents() {
        btn_update_transaction.setOnClickListener {
            val bundle = bundleOf(
                getString(R.string.transaction) to transactionItem,
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
                    transactionViewModel.deleteTransaction(transactionItem.transaction)
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