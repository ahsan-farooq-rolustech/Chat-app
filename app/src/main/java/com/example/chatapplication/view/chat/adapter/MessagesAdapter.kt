package com.example.chatapplication.view.chat.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.data.responseModel.ChatMessageResponseModel
import com.example.chatapplication.databinding.AdapterReceiveMessageBinding
import com.example.chatapplication.databinding.AdapterSendMessageBinding

class MessagesAdapter(private val chatMessageResponsModels: List<ChatMessageResponseModel>, private val receivedImageProfileImage: Bitmap?, private val senderId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object
    {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVE = 2
    }


    class SentMessageViewHolder(val binding: AdapterSendMessageBinding) : RecyclerView.ViewHolder(binding.root)
//    {
//        fun setData(chatMessageResponse: ChatMessageResponseModel)
//        {
//            binding.tvMessage.text = chatMessageResponse.message
//            binding.tvDateTime.text = chatMessageResponse.dateTime
//        }
//
//    }

    class ReceivedMessageViewHolder(val binding: AdapterReceiveMessageBinding) : RecyclerView.ViewHolder(binding.root)
//    {
//        fun setData(chatMessageResponse: ChatMessageResponseModel, receiverImage: Bitmap?)
//        {
//            binding.tvMessage.text = chatMessageResponse.message
//            binding.tvDateTime.text = chatMessageResponse.dateTime
//            if (receiverImage != null) binding.imvProfile.setImageBitmap(receiverImage)
//        }
//    }

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
            (holder as SentMessageViewHolder).binding.chatModel=chatMessageResponsModels[position]
        }
        else
        {
            (holder as ReceivedMessageViewHolder).apply {
                binding.chatModel=chatMessageResponsModels[position]
                binding.profileImage=receivedImageProfileImage
            }
        }
    }

    override fun getItemCount(): Int
    {
        return chatMessageResponsModels.size
    }

    override fun getItemViewType(position: Int): Int
    {
        if (chatMessageResponsModels[position].senderId.equals(senderId))
        {
            return VIEW_TYPE_SENT
        }
        else
        {
            return VIEW_TYPE_RECEIVE
        }
    }
}