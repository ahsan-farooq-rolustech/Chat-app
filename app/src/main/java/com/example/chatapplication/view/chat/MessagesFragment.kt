package com.example.chatapplication.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.chatapplication.utilities.utils.getReadableFormat
import com.example.chatapplication.view.chat.adapter.MessagesAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.util.*


class MessagesFragment : Fragment(), View.OnClickListener, IFirestoreListener
{
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var receivedUserResponseModel: UserResponseModel
    private val args: MessagesFragmentArgs by navArgs<MessagesFragmentArgs>()
    private lateinit var chatMessageResponseModelList: ArrayList<ChatMessageResponseModel>
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var firestoreHelper: FBStoreHelper
    private var userId: String? = null
    private var conversationId:String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentMessagesBinding.inflate(layoutInflater, container, false)
        userId = ChatApplication.fbAuth.currentUser?.email
        loadReceiverDetails()
        setListeners()
        setHelpers()
        initChats()

        listenMessages()
        return binding.root
    }

    private fun initChats()
    {
        if (userId != null)
        {
            setAdapter(userId!!)
        }

    }

    private fun setAdapter(userId: String)
    {
        chatMessageResponseModelList = ArrayList()
        messagesAdapter = MessagesAdapter(chatMessageResponseModelList, receivedUserResponseModel.image.getBitmapFromEncodedString(), userId)
        binding.rvChat.adapter = messagesAdapter
    }

    private fun sendMessage()
    {
        val message = HashMap<String, Any>()
        message[FBConstants.KEY_SENDER_ID] = userId!!
        message[FBConstants.KEY_RECEIVER_ID] = receivedUserResponseModel.email
        message[FBConstants.KEY_MESSAGE] = binding.etInputMessage.text.toString()
        message[FBConstants.KEY_TIMESTAMP] = Date()
        firestoreHelper.uploadMessage(message)
        binding.etInputMessage.setText("")

    }


    private fun setHelpers()
    {
        firestoreHelper = FBStoreHelper()
        firestoreHelper.setListener(this)
    }

    private fun setListeners()
    {
        binding.imvBack.setOnClickListener(this)
        binding.imvSend.setOnClickListener(this)
    }

    private fun loadReceiverDetails()
    {
        receivedUserResponseModel = args.userResponseModel
        binding.tvName.text = receivedUserResponseModel.name
    }

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            binding.imvBack.id ->
            {
                requireActivity().onBackPressed()
            }
            R.id.imvSend ->
            {
                sendMessage()
            }
        }
    }

    //TODO:After testing, transfer all the below code in a new chat helper class

    private val eventListener = EventListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
        if (error != null)
        {
            return@EventListener
        }
        if (value != null)
        {
            val count = chatMessageResponseModelList.size
            for (documentChange in value.documentChanges)
            {
                if (documentChange.type == DocumentChange.Type.ADDED)
                {
                    val chatMessageResponseModel = ChatMessageResponseModel(
                        senderId = documentChange.document.getString(FBConstants.KEY_SENDER_ID) ?: "",
                        receiverId = documentChange.document.getString(FBConstants.KEY_RECEIVER_ID) ?: "",
                        message = documentChange.document.getString(FBConstants.KEY_MESSAGE) ?: "",
                        dateTime = documentChange.document.getDate(FBConstants.KEY_TIMESTAMP)?.getReadableFormat() ?: "",
                        dateObj = documentChange.document.getDate(FBConstants.KEY_TIMESTAMP)
                    )
                    chatMessageResponseModelList.add(chatMessageResponseModel)
                }
            }

            //TODO:Chats are not sorted by date, find how to sort them by date. The below line of code supposed to sort them by date
            //chatMessageResponseModelList.sortWith(Comparator { (_, _, _, _, dateObj): ChatMessageResponseModel, (_, _, _, _, dateObj1): ChatMessageResponseModel -> dateObj!!.compareTo(dateObj1) })
            chatMessageResponseModelList.sortBy {
                it.dateObj
            }
            if (count == 0)
            {
                messagesAdapter.notifyDataSetChanged()
            }
            else
            {
                messagesAdapter.notifyItemRangeInserted(chatMessageResponseModelList.size, chatMessageResponseModelList.size)
                binding.rvChat.smoothScrollToPosition(chatMessageResponseModelList.size - 1)
            }
            binding.rvChat.visibility = View.VISIBLE
        }
        binding.pbLoader.visibility = View.GONE
    }

    private fun listenMessages()
    {
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CHAT)
            .whereEqualTo(FBConstants.KEY_SENDER_ID, userId)
            .whereEqualTo(FBConstants.KEY_RECEIVER_ID, receivedUserResponseModel.id)
            .addSnapshotListener(eventListener)
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CHAT)
            .whereEqualTo(FBConstants.KEY_SENDER_ID, receivedUserResponseModel.id)
            .whereEqualTo(FBConstants.KEY_RECEIVER_ID, userId)
            .addSnapshotListener(eventListener)
    }

    private fun checkForConversation()
    {
        val userEmail=ChatApplication.fbAuth.currentUser?.email
        if(userEmail!=null)
        {
            if(chatMessageResponseModelList.size!=0)
            {
                checkForConversationRemotely(userEmail,receivedUserResponseModel.id)
                checkForConversationRemotely(receivedUserResponseModel.id,userEmail)
            }
        }

    }


    private fun checkForConversationRemotely(senderId:String,receiverId:String)
    {
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CONVERSATIONS).whereEqualTo(FBConstants.KEY_SENDER_ID,senderId)
            .whereEqualTo(FBConstants.KEY_RECEIVER_ID,receiverId)
            .get()
            .addOnCompleteListener(conversationOnCompleteListinner)
    }

    private val conversationOnCompleteListinner=OnCompleteListener<QuerySnapshot>{ task ->
        if(task.isSuccessful&& task.result.documents.size>0)
        {
            val documentSnapshot=task.result.documents[0]
            conversationId=documentSnapshot.id
        }
    }


}