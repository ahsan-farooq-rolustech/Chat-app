package com.example.chatapplication.view.chat.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.data.response.ChatMessageResponse
import com.example.chatapplication.databinding.AdapterReceiveMessageBinding
import com.example.chatapplication.databinding.AdapterSendMessageBinding

class ChatAdapter(private val chatMessageResponses: List<ChatMessageResponse>, private val receivedImageProfileImage: Bitmap?, private val senderId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object
    {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVE = 2
    }


    class SentMessageViewHolder(val binding: AdapterSendMessageBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun setData(chatMessageResponse: ChatMessageResponse)
        {
            binding.tvMessage.text = chatMessageResponse.message
            binding.tvDateTime.text = chatMessageResponse.dateTime
        }

    }

    class ReceivedMessageViewHolder(val binding: AdapterReceiveMessageBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun setData(chatMessageResponse: ChatMessageResponse, receiverImage: Bitmap?)
        {
            binding.tvMessage.text = chatMessageResponse.message
            binding.tvDateTime.text = chatMessageResponse.dateTime
            if (receiverImage != null) binding.imvProfile.setImageBitmap(receiverImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        if (viewType == VIEW_TYPE_SENT)
        {
            return SentMessageViewHolder(AdapterSendMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        else
        {
            return ReceivedMessageViewHolder(AdapterReceiveMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        if (getItemViewType(position) == VIEW_TYPE_SENT)
        {
            (holder as SentMessageViewHolder).setData(chatMessageResponses[position])
        }
        else
        {
            (holder as ReceivedMessageViewHolder).setData(chatMessageResponses[position], receivedImageProfileImage)
        }
    }

    override fun getItemCount(): Int
    {
        return chatMessageResponses.size
    }

    override fun getItemViewType(position: Int): Int
    {
        if (chatMessageResponses[position].senderId.equals(senderId))
        {
            return VIEW_TYPE_SENT
        }
        else
        {
            return VIEW_TYPE_RECEIVE
        }
    }
}