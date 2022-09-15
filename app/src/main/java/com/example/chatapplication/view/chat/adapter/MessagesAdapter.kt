package com.example.chatapplication.view.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.data.responseModel.ChatMessageResponseModel
import com.example.chatapplication.databinding.AdapterReceiveMessageBinding
import com.example.chatapplication.databinding.AdapterSendMessageBinding
import com.example.chatapplication.utilities.utils.AppConstants

class MessagesAdapter(private val mList: ArrayList<ChatMessageResponseModel>, private val senderId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SentMessageViewHolder(val binding: AdapterSendMessageBinding) : RecyclerView.ViewHolder(binding.root)

    class ReceivedMessageViewHolder(val binding: AdapterReceiveMessageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == AppConstants.VIEW_TYPE_SENT) {
            SentMessageViewHolder(AdapterSendMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            ReceivedMessageViewHolder(AdapterReceiveMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == AppConstants.VIEW_TYPE_SENT) {
            (holder as SentMessageViewHolder).binding.chatModel = mList[position]
        } else {
            (holder as ReceivedMessageViewHolder).apply {
                binding.chatModel = mList[position]
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mList[position].senderId == senderId) {
            AppConstants.VIEW_TYPE_SENT
        } else {
            AppConstants.VIEW_TYPE_RECEIVE
        }
    }
}