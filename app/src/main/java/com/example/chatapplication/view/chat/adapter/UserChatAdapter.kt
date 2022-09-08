package com.example.chatapplication.view.chat.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.databinding.UserCharRvLayoutBinding
import com.example.chatapplication.utilities.utils.IUserListener


class UserChatAdapter(private val context: Context,private val userList:ArrayList<com.example.chatapplication.data.model.User>) : RecyclerView.Adapter<UserChatAdapter.ViewHolder>()
{
    private lateinit var mListener:IUserListener


    class ViewHolder(val binding: UserCharRvLayoutBinding) : RecyclerView.ViewHolder(binding.root)

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
        return ViewHolder(UserCharRvLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val data=userList[position]
        holder.binding.apply {
            this.tvName.text=position.toString()
            this.tvEmail.text=data.email
            this.root.setOnClickListener{
                mListener.onUserClicked(data)
            }
            //this.imvImageProfile.setImageBitmap(getUserImage(data.image))
        }
    }

    override fun getItemCount(): Int
    {
        return userList.size
    }


}