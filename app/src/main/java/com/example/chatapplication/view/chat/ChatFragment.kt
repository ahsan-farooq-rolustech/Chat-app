package com.example.chatapplication.view.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatapplication.R
import com.example.chatapplication.databinding.FragmentChatBinding


class ChatFragment : Fragment()
{
    private lateinit var binding:FragmentChatBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding=FragmentChatBinding.inflate(layoutInflater,container,false)

        return binding.root
    }


}