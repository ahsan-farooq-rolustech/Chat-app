package com.example.chatapplication.view.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.data.responseModel.ChatMessageResponseModel
import com.example.chatapplication.data.responseModel.UserResponseModel
import com.example.chatapplication.databinding.AdapterInboxBinding
import com.example.chatapplication.utilities.utils.IUserListener

class InboxAdapter(private val conversationList: ArrayList<ChatMessageResponseModel>) : RecyclerView.Adapter<InboxAdapter.ConversationViewHolder>()
{
    class ConversationViewHolder(val binding: AdapterInboxBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var mListener: IUserListener
    fun setListinners(listener: IUserListener)
    {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder
    {
        return ConversationViewHolder(AdapterInboxBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int)
    {
        holder.binding.chatModel = conversationList[position]
        holder.binding.root.setOnClickListener {
            val user = UserResponseModel(
                id = conversationList[position].conversationId,
                name = conversationList[position].receiverName,
                image = conversationList[position].receiverImageUrl
            )
            mListener.onUserClicked(user)
        }
    }

    override fun getItemCount(): Int
    {
        return conversationList.size
    }
}