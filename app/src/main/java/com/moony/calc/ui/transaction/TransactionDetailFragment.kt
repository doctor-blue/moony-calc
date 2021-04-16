package com.moony.calc.ui.transaction

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentTransactionDetailBinding
import com.moony.calc.model.TransactionItem
import com.moony.calc.ui.dialog.ConfirmDialogBuilder
import com.moony.calc.ui.saving.history.SavingHistoryViewModel
import com.moony.calc.utils.AssetFolderManager
import com.moony.calc.utils.Settings
import com.moony.calc.utils.decimalFormat
import com.moony.calc.utils.formatDateTime
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

    private val binding: FragmentTransactionDetailBinding
        get() = (getViewBinding() as FragmentTransactionDetailBinding)

    private val savingHistoryViewModel: SavingHistoryViewModel by lazy {
        ViewModelProvider(this)[SavingHistoryViewModel::class.java]
    }


    override fun initControls(view: View, savedInstanceState: Bundle?) {
        transactionItem =
                arguments?.getSerializable(getString(R.string.transaction)) as TransactionItem

//        Log.d("TransactionDetail", transaction.idCategory.toString())

        binding.txtTransactionNote.text = transactionItem.transaction.note

        Glide.with(this).load(AssetFolderManager.assetPath + transactionItem.category.iconUrl)
                .into(binding.imgCategory)
        if (transactionItem.category.resId != -1) {
            binding.txtCategoryTitle.setText(transactionItem.category.resId)
        }else{
            binding.txtCategoryTitle.text = transactionItem.category.title
        }

        if (!transactionItem.category.isIncome) {
            binding.txtTransactionMoney.text =
                    ((-1 * transactionItem.transaction.money).decimalFormat() + settings.getString(
                            Settings.SettingKey.CURRENCY_UNIT
                    ))
        } else
            binding.txtTransactionMoney.text =
                    (transactionItem.transaction.money.decimalFormat() + settings.getString(
                            Settings.SettingKey.CURRENCY_UNIT
                    ))

        calendar.set(Calendar.DAY_OF_MONTH, transactionItem.transaction.day)
        calendar.set(Calendar.MONTH, transactionItem.transaction.month)
        calendar.set(Calendar.YEAR, transactionItem.transaction.year)
        binding.txtTransactionDate.text = calendar.formatDateTime()

        calendar.set(Calendar.DAY_OF_MONTH, transactionItem.transaction.day)

        if(transactionItem.category.resId == R.string.saving){
            binding.btnUpdateTransaction.visibility = View.GONE
        }

    }


    override fun initEvents() {
        binding.btnUpdateTransaction.setOnClickListener {
            val bundle = bundleOf(
                    getString(R.string.transaction) to transactionItem,
            )
            findNavController().navigate(
                    R.id.action_transactionDetailFragment_to_updateTransactionFragment,
                    bundle
            )
        }

        binding.toolbarTransactionDetail.setOnMenuItemClickListener {
            if (it.itemId == R.id.mnu_delete) {
                val builder = ConfirmDialogBuilder(requireContext())
                builder.setContent(resources.getString(R.string.notice_delete_transaction))
                val dialog = builder.createDialog()
                builder.btnConfirm.setOnClickListener {
                    transactionViewModel.deleteTransaction(transactionItem.transaction)
                    savingHistoryViewModel.deleteSavingHistoryByTransaction(transactionItem.transaction.idTransaction)

                    dialog.dismiss()
                    requireActivity().onBackPressed()
                }
                builder.btnCancel.setOnClickListener { dialog.dismiss() }
                builder.showDialog()
            }
            true
        }

        binding.toolbarTransactionDetail.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

    }


    override fun getLayoutId(): Int = R.layout.fragment_transaction_detail

}