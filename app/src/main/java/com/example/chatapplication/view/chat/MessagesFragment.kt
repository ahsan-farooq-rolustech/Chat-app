package com.example.chatapplication.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.R
import com.example.chatapplication.data.responseModel.ChatMessageResponseModel
import com.example.chatapplication.data.responseModel.UserResponseModel
import com.example.chatapplication.databinding.FragmentMessagesBinding
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.utils.FBConstants
import com.example.chatapplication.utilities.utils.IFirestoreListener
import com.example.chatapplication.utilities.utils.getBitmapFromEncodedString
import com.example.chatapplication.utilities.utils.showToastMessage
import com.example.chatapplication.view.chat.adapter.MessagesAdapter
import java.util.*
import kotlin.collections.ArrayList


class MessagesFragment : Fragment(), View.OnClickListener, IFirestoreListener {
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var receivedUserResponseModel: UserResponseModel
    private val args: MessagesFragmentArgs by navArgs<MessagesFragmentArgs>()
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var helper: FBStoreHelper
    private var userId: String? = null
    private var conversationId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        binding = FragmentMessagesBinding.inflate(layoutInflater, container, false)
        userId = ChatApplication.fbAuth.currentUser?.email
        loadReceiverDetails()
        setListeners()
        setHelpers()
        //initChats()
        getMessages()
        checkConversationExistance()
        return binding.root
    }

    private fun getMessages() {

    }

    private fun checkConversationExistance() {
        if (userId != null) helper.checkIfConversationExists(userId!!, receivedUserResponseModel.id)
    }

//    private fun initChats() {
//        if (userId != null) {
//            setAdapter(, messagesList)
//        }
//
//    }

    private fun setAdapter( messagesList: ArrayList<ChatMessageResponseModel>) {
        messagesAdapter = MessagesAdapter(messagesList, receivedUserResponseModel.image.getBitmapFromEncodedString(), userId!!)
        binding.rvChat.adapter = messagesAdapter
        messagesAdapter.notifyDataSetChanged()
        binding.rvChat.visibility=View.VISIBLE
        isLoading(false)
    }

    private fun sendMessage() {
        val message = HashMap<String, Any>()
        message[FBConstants.KEY_SENDER_ID] = userId!!
        message[FBConstants.KEY_RECEIVER_ID] = receivedUserResponseModel.email
        message[FBConstants.KEY_MESSAGE] = binding.etInputMessage.text.toString()
        message[FBConstants.KEY_TIMESTAMP] = Date()
        if(conversationId!=null)
        {
            helper.uploadMessage(message, conversationId!!)
            binding.etInputMessage.setText("")
        }
        else
        {
            //for testing purposes
            requireContext().showToastMessage("conversation id is null")
        }


    }


    private fun setHelpers() {
        helper = FBStoreHelper()
        helper.setListener(this)
    }

    private fun setListeners() {
        binding.imvBack.setOnClickListener(this)
        binding.imvSend.setOnClickListener(this)
    }

    private fun loadReceiverDetails() {
        receivedUserResponseModel = args.userResponseModel
        binding.tvName.text = receivedUserResponseModel.name
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.imvBack.id -> {
                requireActivity().onBackPressed()
            }
            R.id.imvSend -> {
                sendMessage()
            }
        }
    }

    private fun isLoading(isLoading:Boolean)
    {
        if(isLoading) binding.pbLoader.visibility=View.GONE
        else binding.pbLoader.visibility=View.GONE
    }

    override fun onConversationGetSuccess(conversationId: String) {
        this.conversationId=conversationId
        helper.getMessages(this.conversationId!!)
    }

    override fun onGetMessagesSuccessful(messagesList: ArrayList<ChatMessageResponseModel>) {
        setAdapter(messagesList)
    }

//    //TODO:After testing, transfer all the below code in a new chat helper class
//
//    private val eventListener = EventListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
//        if (error != null) {
//            return@EventListener
//        }
//        if (value != null) {
//            val count = chatMessageResponseModelList.size
//            for (documentChange in value.documentChanges) {
//                if (documentChange.type == DocumentChange.Type.ADDED) {
//                    val chatMessageResponseModel = ChatMessageResponseModel(senderId = documentChange.document.getString(FBConstants.KEY_SENDER_ID) ?: "", receiverId = documentChange.document.getString(FBConstants.KEY_RECEIVER_ID) ?: "", message = documentChange.document.getString(FBConstants.KEY_MESSAGE) ?: "", dateTime = documentChange.document.getDate(FBConstants.KEY_TIMESTAMP)?.getReadableFormat() ?: "", dateObj = documentChange.document.getDate(FBConstants.KEY_TIMESTAMP))
//                    chatMessageResponseModelList.add(chatMessageResponseModel)
//                }
//            }
//
//            //TODO:Chats are not sorted by date, find how to sort them by date. The below line of code supposed to sort them by date
//            //chatMessageResponseModelList.sortWith(Comparator { (_, _, _, _, dateObj): ChatMessageResponseModel, (_, _, _, _, dateObj1): ChatMessageResponseModel -> dateObj!!.compareTo(dateObj1) })
//            chatMessageResponseModelList.sortBy {
//                it.dateObj
//            }
//            if (count == 0) {
//                messagesAdapter.notifyDataSetChanged()
//            } else {
//                messagesAdapter.notifyItemRangeInserted(chatMessageResponseModelList.size, chatMessageResponseModelList.size)
//                binding.rvChat.smoothScrollToPosition(chatMessageResponseModelList.size - 1)
//            }
//            binding.rvChat.visibility = View.VISIBLE
//        }
//        binding.pbLoader.visibility = View.GONE
//    }
//
//    private fun listenMessages() {
//        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CHAT).whereEqualTo(FBConstants.KEY_SENDER_ID, userId).whereEqualTo(FBConstants.KEY_RECEIVER_ID, receivedUserResponseModel.id).addSnapshotListener(eventListener)
//        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CHAT).whereEqualTo(FBConstants.KEY_SENDER_ID, receivedUserResponseModel.id).whereEqualTo(FBConstants.KEY_RECEIVER_ID, userId).addSnapshotListener(eventListener)
//    }


    override fun onChatCreatedSuccess() {
        TODO("Not yet implemented")
    }


}