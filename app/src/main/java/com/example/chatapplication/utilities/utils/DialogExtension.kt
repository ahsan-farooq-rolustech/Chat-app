package com.example.chatapplication.utilities.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.example.chatapplication.R
import com.example.chatapplication.data.responseModel.DialogItems
import com.example.chatapplication.databinding.DialogConfirmationBinding


fun Context.showConfirmationDialog(model: DialogItems, listener: IDialogListeners) {
    val dialog = Dialog(this, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogConfirmationBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_confirmation, null, false)
    dialog.setContentView(binding.root)
    binding.model = model
    binding.btnYes.setOnClickListener {
        dialog.dismiss()
        listener.onClickPositiveButton()
    }

    binding.btnNo.setOnClickListener {
        dialog.dismiss()
        listener.onClickNegativeButton()
    }

    dialog.show()
}