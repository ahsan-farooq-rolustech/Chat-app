package com.example.chatapplication.view.chat.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.databinding.AdapterUserBinding

import com.example.chatapplication.utilities.utils.IUserListener


class UserChatAdapter(private val context: Context,private val userResponseList:ArrayList<com.example.chatapplication.data.response.UserResponse>) : RecyclerView.Adapter<UserChatAdapter.ViewHolder>()
{
    private lateinit var mListener:IUserListener


    class ViewHolder(val binding: AdapterUserBinding) : RecyclerView.ViewHolder(binding.root)

    fun setListener(listener: IUserListener)
    {
        mListener=listener
    }

    private fun getUserImage(encodedImage: String): Bitmap?
    {
        val bytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(AdapterUserBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.binding.model = userResponseList[position]
        holder.binding.clMain.setOnClickListener{
            mListener.onUserClicked(userResponseList[position])
        }
    }

    override fun getItemCount(): Int
    {
        return userResponseList.size
    }


}