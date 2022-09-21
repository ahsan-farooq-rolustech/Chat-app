package com.example.chatapplication.view.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.R
import com.example.chatapplication.data.responseModel.DialogItems
import com.example.chatapplication.data.responseModel.InboxResponseModel
import com.example.chatapplication.data.responseModel.UserResponseModel
import com.example.chatapplication.databinding.FragmentInboxBinding
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.utils.*
import com.example.chatapplication.view.authentication.AuthActivity
import com.example.chatapplication.view.base.ActivityBase
import com.example.chatapplication.view.chat.adapter.InboxAdapter


class InboxFragment : Fragment(), IFBAuthListener, IFirestoreListener, View.OnClickListener, IInboxListener, IBottomSheetListeners, IDialogListeners {

    private lateinit var binding: FragmentInboxBinding
    private lateinit var fsHelper: FBStoreHelper
    private var mList = ArrayList<InboxResponseModel>()
    private lateinit var adapter: InboxAdapter
    private var id: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInboxBinding.inflate(layoutInflater, container, false)
        initThings()
        setListeners()
        return binding.root
    }

    private fun initThings() {
        binding.isLoading = true
        binding.isInboxEmpty = false
        binding.userImageUrl = ChatApplication.db.getString(UserPrefConstants.IMAGE_URL)
        binding.userStatus = AppConstants.STATUS_ACTIVE
        fsHelper = FBStoreHelper()
        fsHelper.setListener(this)
        fsHelper.setStatus(AppConstants.STATUS_ACTIVE)
        fsHelper.getConversations(ChatApplication.fbAuth.currentUser?.email!!)
        setAdapter()
    }

    private fun setListeners() {
        binding.fabNewChat.setOnClickListener(this)
        binding.imvImageSignout.setOnClickListener(this)
    }

    private fun setAdapter() {
        adapter = InboxAdapter(mList)
        binding.rvInbox.adapter = adapter
        adapter.setListeners(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabNewChat -> {
                val action = InboxFragmentDirections.actionChatFragmentToUsersFragment()
                Navigation.findNavController(v).navigate(action)
            }
            R.id.imvImageSignout -> signOut()

        }
    }

    private fun signOut() {
        ChatApplication.fbAuth.signOut()
        ActivityBase.activity.apply {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

    override fun onGetUserInboxSuccessful(list: ArrayList<InboxResponseModel>) {
        binding.isLoading = false
        if (list.isNotEmpty()) {
            binding.isInboxEmpty = false
            mList.clear()
            mList.addAll(list.filter { it.lastMsg.isNotEmpty() })
            mList.sortByDescending {
                it.lastMessageDateObj
            }
            adapter.notifyDataSetChanged()
        } else {
            binding.isInboxEmpty = true
        }
    }

    override fun onGetUserInboxFailure(error: String) {
        ActivityBase.activity.showToastMessage(AppAlerts.GET_INBOX_FAILURE)
    }

    override fun onClickConversation(position: Int) {
        val inbox = mList[position]
        val userEmail = ChatApplication.db.getString(UserPrefConstants.EMAIL)
        if (userEmail != null) {
            val user = if (inbox.user1Email == userEmail) {
                UserResponseModel(name = inbox.user2Name, email = inbox.user2EMail, image = inbox.user2ImageUrl, id = inbox.user2EMail)
            } else {
                UserResponseModel(name = inbox.user1Name, email = inbox.user1Email, image = inbox.user1ImageUrl, id = inbox.user1Email)
            }
            val action = InboxFragmentDirections.actionInboxFragmentToChatFragment(user)
            findNavController().navigate(action)
        }
    }

    override fun onClickDeleteConversation(position: Int) {
        deleteChat(mList[position].conversationId)
    }


    private fun deleteChat(conversationId: String) {
        id = conversationId
        ActivityBase.activity.showConfirmationDialog(DialogItems(title = "", description = AppAlerts.CHAT_DELETE_CONFORMATION, buttonPositive = "Yes", buttonNegative = "No"), this)
    }

    override fun onClickPositiveButton() {
        binding.isLoading = true
        fsHelper.deleteConversation(id)
    }

    override fun onConversationDeletedSuccess() {
        binding.isLoading = false
        ActivityBase.activity.showToastMessage(AppAlerts.CONVERSATION_DELETE_SUCCESS)
        val index = mList.indexOf(mList.find { it.conversationId == id })
        mList.removeAt(index)
        adapter.notifyItemRemoved(index)
        if (mList.size == 0) binding.isInboxEmpty = true
    }

}