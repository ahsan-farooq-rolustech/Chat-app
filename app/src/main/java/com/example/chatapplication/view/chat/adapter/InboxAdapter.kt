package com.example.chatapplication.view.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.data.responseModel.InboxResponseModel
import com.example.chatapplication.databinding.AdapterInboxBinding
import com.example.chatapplication.utilities.helperClasses.ViewBinderHelper
import com.example.chatapplication.utilities.utils.IInboxListener


class InboxAdapter(private val conversationList: ArrayList<InboxResponseModel>) : RecyclerView.Adapter<InboxAdapter.ConversationViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()

    class ConversationViewHolder(val binding: AdapterInboxBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var mListener: IInboxListener

    fun setListeners(listener: IInboxListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        return ConversationViewHolder(AdapterInboxBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        viewBinderHelper.bind(holder.binding.srlInbox, conversationList[position].conversationId);
        viewBinderHelper.setOpenOnlyOne(true)
        holder.binding.model = conversationList[position]
        holder.binding.clMain.setOnClickListener {
            mListener.onClickConversation(position)
        }

        holder.binding.rlDelete.setOnClickListener {
            holder.binding.srlInbox.close(true)
            mListener.onClickDeleteConversation(position)
        }
    }

    override fun getItemCount(): Int {
        return conversationList.size
    }
}