package com.example.chatapplication.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.chatapplication.R
import com.example.chatapplication.data.model.User
import com.example.chatapplication.databinding.FragmentChatBinding
import com.example.chatapplication.utilities.utils.AppConstants


class ChatFragment : Fragment(), View.OnClickListener
{
    private lateinit var binding: FragmentChatBinding
    private lateinit var receivedUser: User
    private val args:ChatFragmentArgs by navArgs<ChatFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        loadReceiverDetails()
        setListeners()
        return binding.root
    }

    private fun setListeners()
    {
        binding.imvBack.setOnClickListener(this)
    }

    private fun loadReceiverDetails()
    {
        receivedUser = args.user
        binding.tvName.text = receivedUser.name
    }

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            binding.imvBack.id ->
            {
                requireActivity().onBackPressed()
            }
        }
    }


}