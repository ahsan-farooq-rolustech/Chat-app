package com.example.chatapplication.view.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.R
import com.example.chatapplication.data.responseModel.ChatMessageResponseModel
import com.example.chatapplication.data.responseModel.InboxResponseModel
import com.example.chatapplication.databinding.FragmentInboxBinding
import com.example.chatapplication.utilities.helperClasses.FBAuthHelper
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.utils.*
import com.example.chatapplication.view.MainActivity
import com.example.chatapplication.view.authentication.AuthActivity
import com.example.chatapplication.view.base.ActivityBase
import com.example.chatapplication.view.chat.adapter.InboxAdapter
import com.google.firebase.messaging.FirebaseMessaging


class InboxFragment : Fragment(), IFBAuthListener, IFirestoreListener, View.OnClickListener
{

    private lateinit var binding: FragmentInboxBinding
    private lateinit var authHelper: FBAuthHelper
    private lateinit var firestoreHelper: FBStoreHelper
    private lateinit var conversations: ArrayList<ChatMessageResponseModel>
    private lateinit var inboxAdapter: InboxAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentInboxBinding.inflate(layoutInflater, container, false)
        setHelpers()
        setListeners()
        getToken()
        setUserStatus()
        getConversations()
        firestoreHelper.getConversations(ChatApplication.fbAuth.currentUser?.email!!)
        return binding.root
    }

    private fun getConversations() {
        firestoreHelper
    }

    private fun setUserStatus()
    {
        firestoreHelper.setStatus(AppConstants.STATUS_ACTIVE)
    }

    private fun setListeners()
    {
//        binding.fabNewChat.setOnClickListener{
//            Toast.makeText(requireContext(), "floating action button clicked", Toast.LENGTH_SHORT).show()
//            val action = ChatOfUsersFragmentDirections.actionChatFragmentToUsersFragment()
//            Navigation.findNavController(it).navigate(action)
//        }
        binding.fabNewChat.setOnClickListener(this)
        binding.imvImageSignout.setOnClickListener(this)
    }

    private fun setHelpers()
    {
        authHelper = FBAuthHelper(requireContext())
        authHelper.setListener(this)
        firestoreHelper = FBStoreHelper()
        firestoreHelper.setListener(this)
    }

    private fun setAdapter(inboxList: ArrayList<ChatMessageResponseModel>)
    {
        inboxAdapter = InboxAdapter(inboxList)
        binding.rvConversation.adapter = inboxAdapter
        inboxAdapter.notifyDataSetChanged()
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
                val action = InboxFragmentDirections.actionChatFragmentToUsersFragment()
                Navigation.findNavController(v).navigate(action)
            }

            R.id.imvImageSignout ->
            {
                signOut()
            }
        }
    }

    private fun signOut()
    {
        ChatApplication.fbAuth.signOut()
        ActivityBase.activity.apply {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

    override fun onChatCreatedSuccess() {
        TODO("Not yet implemented")
    }

    override fun onGetUserInboxSuccessful(list: ArrayList<InboxResponseModel>) {

    }

    override fun onGetUserInboxFailure(error: String) {
        ActivityBase.activity.showToastMessage(AppAlerts.GET_INBOX_FAILURE)
    }

    //    override fun onPause()
//    {
//        super.onPause()
//        firestoreHelper.setStatus(AppConstants.STATUS_IN_ACTIVE)
//    }
//
//    override fun onDestroy()
//    {
//        super.onDestroy()
//        firestoreHelper.setStatus(AppConstants.STATUS_OFFLINE)
//    }
//
//    override fun onDestroyView()
//    {
//        super.onDestroyView()
//        firestoreHelper.setStatus(AppConstants.STATUS_OFFLINE)
//    }


}