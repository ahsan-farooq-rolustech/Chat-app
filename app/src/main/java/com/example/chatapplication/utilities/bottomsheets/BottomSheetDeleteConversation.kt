package com.example.chatapplication.utilities.bottomsheets

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.chatapplication.R
import com.example.chatapplication.databinding.BottomSheetDeleteConversationBinding
import com.example.chatapplication.utilities.utils.IBottomSheetListeners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDeleteConversation constructor(private val con: Context):BottomSheetDialogFragment() {
    lateinit var binding:BottomSheetDeleteConversationBinding
    lateinit var mListener:IBottomSheetListeners

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding=DataBindingUtil.inflate(LayoutInflater.from(con), R.layout.bottom_sheet_delete_conversation,null,false)
        dialog.setContentView(binding.root)
        (binding.root.parent as View).setBackgroundColor(ContextCompat.getColor(con,android.R.color.transparent))

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnDelete.setOnClickListener {
            dialog.dismiss()
            mListener.onClickDeleteChat()
        }

    }

    fun setListener(listener:IBottomSheetListeners)
    {
        mListener=listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state=BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.isDraggable=false
        return dialog
    }

}