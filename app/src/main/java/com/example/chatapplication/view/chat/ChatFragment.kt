package com.example.chatapplication.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.chatapplication.R
import com.example.chatapplication.databinding.FragmentChatBinding
import com.example.chatapplication.utilities.helperClasses.FBAuthHelper
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.utils.IFBAuthListener
import com.example.chatapplication.utilities.utils.IFirestoreListinner
import com.google.firebase.messaging.FirebaseMessaging


class ChatFragment : Fragment(), IFBAuthListener,IFirestoreListinner, View.OnClickListener
{

    private lateinit var binding: FragmentChatBinding
    private lateinit var authHelper: FBAuthHelper
    private lateinit var firestoreHelper: FBStoreHelper


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        setHelpers()
        setListeners()
        getToken()
        return binding.root
    }

    private fun setListeners()
    {
        binding.fabNewChat.setOnClickListener{
            Toast.makeText(requireContext(), "floating action button clicked", Toast.LENGTH_SHORT).show()
            val action = ChatFragmentDirections.actionChatFragmentToUsersFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun setHelpers()
    {
        authHelper = FBAuthHelper(requireContext())
        authHelper.setListener(this)
        firestoreHelper = FBStoreHelper()
        firestoreHelper.setListener(this)
    }

    fun getToken()
    {
        FirebaseMessaging.getInstance().token.addOnSuccessListener(this::updateToken)
    }

    fun updateToken(token: String)
    {
        firestoreHelper.updateToken(token)
    }

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            R.id.fabNewChat ->
            {
//                val action = ShowAllChatFragmentDirections.actionChatFragmentToUsersFragment()
//                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }


}