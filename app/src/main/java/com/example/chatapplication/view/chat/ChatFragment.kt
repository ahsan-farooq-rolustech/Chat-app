package com.example.chatapplication.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.R
import com.example.chatapplication.data.model.ChatMessage
import com.example.chatapplication.data.model.User
import com.example.chatapplication.databinding.FragmentChatBinding
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.utils.FBConstants
import com.example.chatapplication.utilities.utils.IFirestoreListener
import com.example.chatapplication.utilities.utils.getBitmapFromEncodedString
import com.example.chatapplication.utilities.utils.getReadableFormat
import com.example.chatapplication.view.chat.adapter.ChatAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.util.*


class ChatFragment : Fragment(), View.OnClickListener, IFirestoreListener
{
    private lateinit var binding: FragmentChatBinding
    private lateinit var receivedUser: User
    private val args: ChatFragmentArgs by navArgs<ChatFragmentArgs>()
    private lateinit var chatMessages: ArrayList<ChatMessage>
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var firestoreHelper: FBStoreHelper
    private var userId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        userId= ChatApplication.fbAuth.currentUser?.email
        loadReceiverDetails()
        setListeners()
        setHelpers()
        initChats()

        listenMessages()
        return binding.root
    }

    private fun initChats()
    {
        if(userId!=null)
        {
            setAdapter(userId!!)
        }

    }

    private fun setAdapter(userId: String)
    {
        chatMessages=ArrayList()
        chatAdapter= ChatAdapter(chatMessages,receivedUser.image.getBitmapFromEncodedString(), userId)
        binding.rvChat.adapter=chatAdapter
    }

    private fun sendMessage()
    {
        val message=HashMap<String,Any>()
        message[FBConstants.KEY_SENDER_ID]=userId!!
        message[FBConstants.KEY_RECEIVER_ID]=receivedUser.email
        message[FBConstants.KEY_MESSAGE]=binding.etInputMessage.text.toString()
        message[FBConstants.KEY_TIMESTAMP]=Date()
        firestoreHelper.uploadMessage(message)

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
            R.id.imvSend->
            {
                sendMessage()
            }
        }
    }

    //TODO:After testing, transfer all this in a new chat helper class
    private val eventListener = EventListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
        if (error != null)
        {
            return@EventListener
        }
        if (value != null)
        {
            val count = chatMessages.size
            for(documentChange in value.documentChanges)
            {
                if(documentChange.type==DocumentChange.Type.ADDED)
                {
                    val chatMessage=ChatMessage(
                        senderId = documentChange.document.getString(FBConstants.KEY_SENDER_ID)?:"",
                        receiverId = documentChange.document.getString(FBConstants.KEY_RECEIVER_ID)?:"",
                        message = documentChange.document.getString(FBConstants.KEY_MESSAGE)?:"",
                        dateTime = documentChange.document.getDate(FBConstants.KEY_TIMESTAMP)?.getReadableFormat() ?:"",
                        dateObj = documentChange.document.getDate(FBConstants.KEY_TIMESTAMP)
                    )
                    chatMessages.add(chatMessage)
                }
            }
            chatMessages.sortWith(Comparator { (_, _, _, _, dateObj): ChatMessage, (_, _, _, _, dateObj1): ChatMessage -> dateObj!!.compareTo(dateObj1) })
            if(count==0)
            {
                chatAdapter.notifyDataSetChanged()
            }
            else
            {
                chatAdapter.notifyItemRangeInserted(chatMessages.size,chatMessages.size)
                binding.rvChat.smoothScrollToPosition(chatMessages.size-1)
            }
            binding.rvChat.visibility=View.VISIBLE
        }
        binding.pbLoader.visibility=View.GONE
    }

    private fun listenMessages()
    {
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CHAT)
            .whereEqualTo(FBConstants.KEY_SENDER_ID,userId)
            .whereEqualTo(FBConstants.KEY_RECEIVER_ID,receivedUser.id)
            .addSnapshotListener(eventListener)
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CHAT)
            .whereEqualTo(FBConstants.KEY_SENDER_ID,receivedUser.id)
            .whereEqualTo(FBConstants.KEY_RECEIVER_ID,userId)
            .addSnapshotListener(eventListener)
    }

}