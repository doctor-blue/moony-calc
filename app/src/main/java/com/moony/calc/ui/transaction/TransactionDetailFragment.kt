package com.moony.calc.ui.transaction

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.moony.calc.R
import com.moony.calc.base.BaseFragment
import com.moony.calc.databinding.FragmentTransactionDetailBinding
import com.moony.calc.model.TransactionItem
import com.moony.calc.ui.saving.history.SavingHistoryViewModel
import com.moony.calc.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TransactionDetailFragment : BaseFragment() {

    private val transactionViewModel: TransactionViewModel by activityViewModels()

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

        binding.txtCategoryTitle.text = transactionItem.category.title

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

        calendar.time = transactionItem.transaction.transactionTime
        binding.txtTransactionDate.text = calendar.formatDateTime()

        if (transactionItem.category.resId == R.string.saving) {
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
                requireContext().showDialogDelete {
                    transactionViewModel.deleteTransaction(transactionItem.transaction)
                    savingHistoryViewModel.deleteSavingHistoryByTransaction(transactionItem.transaction.idTransaction)

                    requireActivity().onBackPressed()
                }
            }
            true
        }

        binding.toolbarTransactionDetail.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

    }


    override fun getLayoutId(): Int = R.layout.fragment_transaction_detail

}