package com.moony.calc.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.moony.calc.R

class ConfirmDialogBuilder @SuppressLint("InflateParams") constructor(context: Context) :
    AlertDialog.Builder(context) {


    private val rootView: View = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null)
    val btnConfirm: MaterialButton
    val btnCancel: MaterialButton
    private val txtTitle: MaterialTextView
    private val txtContent: MaterialTextView
    private lateinit var dialog: AlertDialog

    init {
        btnConfirm = rootView.findViewById(R.id.btn_confirm_confirm_dialog)
        btnCancel = rootView.findViewById(R.id.btn_cancel_confirm_dialog)
        txtContent = rootView.findViewById(R.id.txt_content_confirm_dialog)
        txtTitle = rootView.findViewById(R.id.txt_title_confirm_dialog)
        setView(rootView)
    }

    fun createDialog(): AlertDialog {
        dialog = create()
        return dialog
    }

    fun setTitle(title: String) {
        txtTitle.text = title
    }

    fun setContent(content: String) {
        txtContent.text = content
    }

    fun showDialog() {
        dialog.show()
    }

}