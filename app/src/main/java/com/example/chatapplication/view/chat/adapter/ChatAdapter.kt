package com.example.chatapplication.view.chat.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.data.model.ChatMessage
import com.example.chatapplication.databinding.ItemContainerReceivedMessageBinding
import com.example.chatapplication.databinding.ItemContainerSentMessageBinding

class ChatAdapter(private val chatMessages:List<ChatMessage>,private val receivedImageProfileImage:Bitmap,private val senderId:String):RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object
    {
        private const val VIEW_TYPE_SENT=1
        private const val VIEW_TYPE_RECEIVE=2
    }


    class SentMessageViewHolder(val binding:ItemContainerSentMessageBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun setData(chatMessage: ChatMessage)
        {
            binding.tvMessage.text=chatMessage.message
            binding.tvDateTime.text=chatMessage.dateTime
        }

    }

    class ReceivedMessageViewHolder(val binding: ItemContainerReceivedMessageBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun setData(chatMessage: ChatMessage,receiverImage:Bitmap)
        {
            binding.tvMessage.text=chatMessage.message
            binding.tvDateTime.text=chatMessage.dateTime
            binding.imvProfile.setImageBitmap(receiverImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        if(viewType== VIEW_TYPE_SENT)
        {
            return SentMessageViewHolder(ItemContainerSentMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
        else
        {
            return ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        if(getItemViewType(position)== VIEW_TYPE_SENT)
        {
            (holder as SentMessageViewHolder ).setData(chatMessages[position])
        }
        else
        {
            (holder as ReceivedMessageViewHolder).setData(chatMessages[position],receivedImageProfileImage)
        }
    }

    override fun getItemCount(): Int
    {
        return chatMessages.size
    }

    override fun getItemViewType(position: Int): Int
    {
        if(chatMessages[position].senderId.equals(senderId))
        {
            return VIEW_TYPE_SENT
        }
        else
        {
            return VIEW_TYPE_RECEIVE
        }
    }
}