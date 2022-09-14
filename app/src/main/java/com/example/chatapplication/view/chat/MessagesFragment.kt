package com.example.chatapplication.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.R
import com.example.chatapplication.data.responseModel.ChatMessageResponseModel
import com.example.chatapplication.data.responseModel.UserResponseModel
import com.example.chatapplication.databinding.FragmentMessagesBinding
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.utils.AppAlerts
import com.example.chatapplication.utilities.utils.FBConstants
import com.example.chatapplication.utilities.utils.IFirestoreListener
import com.example.chatapplication.view.chat.adapter.MessagesAdapter
import java.util.*


class MessagesFragment : Fragment(), View.OnClickListener, IFirestoreListener {
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var receivedUserResponseModel: UserResponseModel
    private val args: MessagesFragmentArgs by navArgs()
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var helper: FBStoreHelper
    private var userId: String? = null
    private var conversationId: String? = null
    private var messagesList = ArrayList<ChatMessageResponseModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        binding = FragmentMessagesBinding.inflate(layoutInflater, container, false)
        initThings()
        setListeners()
        return binding.root
    }

    private fun initThings() {
        userId = ChatApplication.fbAuth.currentUser?.email
        receivedUserResponseModel = args.userResponseModel
        binding.tvName.text = receivedUserResponseModel.name

        helper = FBStoreHelper()
        helper.setListener(this)

        if (userId != null) helper.checkIfConversationExists(userId!!, receivedUserResponseModel)
        setAdapter()
    }

    private fun setAdapter() {
        messagesAdapter = MessagesAdapter(messagesList, userId!!)
        binding.rvChat.adapter = messagesAdapter
        binding.rvChat.visibility = View.VISIBLE
        isLoading(false)
    }

    private fun sendMessage() {
        val text=binding.etInputMessage.text.toString()
        if(text.isEmpty())
        {
            binding.etInputMessage.error=AppAlerts.EMPTY_MESSAGE
            return
        }
        val message = HashMap<String, Any>()
        message[FBConstants.KEY_SENDER_ID] = userId!!
        message[FBConstants.KEY_RECEIVER_ID] = receivedUserResponseModel.email
        message[FBConstants.KEY_MESSAGE] = binding.etInputMessage.text.toString()
        message[FBConstants.KEY_TIMESTAMP] = Date()
        if (conversationId != null) {
            helper.uploadMessage(message, conversationId!!)
            binding.etInputMessage.setText("")
        }
    }


    private fun setHelpers() {

    }

    private fun setListeners() {
        binding.imvBack.setOnClickListener(this)
        binding.imvSend.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.imvBack.id -> {
                onBackPressed()
            }
            R.id.imvSend -> {
                sendMessage()
            }
        }
    }

    private fun onBackPressed() {
        val action=MessagesFragmentDirections.actionChatFragmentToChatOfUsersFragment()
        findNavController().navigate(action)
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoader.visibility = View.GONE
        else binding.pbLoader.visibility = View.GONE
    }

    override fun onConversationGetSuccess(conversationId: String) {
        this.conversationId = conversationId
        helper.getMessages(this.conversationId!!)
    }

    override fun onGetMessagesSuccessful(list: ArrayList<ChatMessageResponseModel>) {
        messagesList.clear()
        messagesList.addAll(list)
        messagesList.map { it.receiverImageUrl = receivedUserResponseModel.image }
        messagesAdapter.notifyItemRangeChanged(0, messagesList.size)
        binding.rvChat.scrollToPosition(messagesList.size - 1)
    }
}