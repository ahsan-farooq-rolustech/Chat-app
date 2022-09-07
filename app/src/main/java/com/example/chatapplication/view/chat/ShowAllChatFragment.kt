package com.example.chatapplication.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chatapplication.R
import com.example.chatapplication.databinding.FragmentShowAllChatBinding
import com.example.chatapplication.utilities.helperClasses.FBAuthHelper
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.utils.IFBAuthListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowAllChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowAllChatFragment : Fragment(), IFBAuthListener
{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding:FragmentShowAllChatBinding
    private lateinit var authHelper: FBAuthHelper
    private lateinit var firestoreHelper: FBStoreHelper

    companion object
    {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShowAllChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) = ShowAllChatFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding=FragmentShowAllChatBinding.inflate(layoutInflater,container,false)
        setHelpers()
        getToken()
        return inflater.inflate(R.layout.fragment_show_all_chat, container, false)
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

    fun updateToken(token:String)
    {
        firestoreHelper.updateToken(token)
    }



}