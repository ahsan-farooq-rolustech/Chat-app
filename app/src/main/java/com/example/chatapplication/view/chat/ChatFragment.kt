package com.example.chatapplication.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.R
import com.example.chatapplication.data.response.ChatMessageResponse
import com.example.chatapplication.data.response.UserResponse
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
    private lateinit var receivedUserResponse: UserResponse
    private val args: ChatFragmentArgs by navArgs<ChatFragmentArgs>()
    private lateinit var chatMessageResponseList: ArrayList<ChatMessageResponse>
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
        chatMessageResponseList=ArrayList()
        chatAdapter= ChatAdapter(chatMessageResponseList,receivedUserResponse.image.getBitmapFromEncodedString(), userId)
        binding.rvChat.adapter=chatAdapter
    }

    private fun sendMessage()
    {
        val message=HashMap<String,Any>()
        message[FBConstants.KEY_SENDER_ID]=userId!!
        message[FBConstants.KEY_RECEIVER_ID]=receivedUserResponse.email
        message[FBConstants.KEY_MESSAGE]=binding.etInputMessage.text.toString()
        message[FBConstants.KEY_TIMESTAMP]=Date()
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
        receivedUserResponse = args.userResponse
        binding.tvName.text = receivedUserResponse.name
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

    //TODO:After testing, transfer all the below code in a new chat helper class

    private val eventListener = EventListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
        if (error != null)
        {
            return@EventListener
        }
        if (value != null)
        {
            val count = chatMessageResponseList.size
            for(documentChange in value.documentChanges)
            {
                if(documentChange.type==DocumentChange.Type.ADDED)
                {
                    val chatMessageResponse=ChatMessageResponse(
                        senderId = documentChange.document.getString(FBConstants.KEY_SENDER_ID)?:"",
                        receiverId = documentChange.document.getString(FBConstants.KEY_RECEIVER_ID)?:"",
                        message = documentChange.document.getString(FBConstants.KEY_MESSAGE)?:"",
                        dateTime = documentChange.document.getDate(FBConstants.KEY_TIMESTAMP)?.getReadableFormat() ?:"",
                        dateObj = documentChange.document.getDate(FBConstants.KEY_TIMESTAMP)
                    )
                    chatMessageResponseList.add(chatMessageResponse)
                }
            }

            //TODO:Chats are not sorted by date, find how to sort them by date. The below line of code supposed to sort them by date
            //chatMessageResponseList.sortWith(Comparator { (_, _, _, _, dateObj): ChatMessageResponse, (_, _, _, _, dateObj1): ChatMessageResponse -> dateObj!!.compareTo(dateObj1) })
            chatMessageResponseList.sortBy {
                it.dateObj
            }
            if(count==0)
            {
                chatAdapter.notifyDataSetChanged()
            }
            else
            {
                chatAdapter.notifyItemRangeInserted(chatMessageResponseList.size,chatMessageResponseList.size)
                binding.rvChat.smoothScrollToPosition(chatMessageResponseList.size-1)
            }
            binding.rvChat.visibility=View.VISIBLE
        }
        binding.pbLoader.visibility=View.GONE
    }

    private fun listenMessages()
    {
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CHAT)
            .whereEqualTo(FBConstants.KEY_SENDER_ID,userId)
            .whereEqualTo(FBConstants.KEY_RECEIVER_ID,receivedUserResponse.id)
            .addSnapshotListener(eventListener)
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CHAT)
            .whereEqualTo(FBConstants.KEY_SENDER_ID,receivedUserResponse.id)
            .whereEqualTo(FBConstants.KEY_RECEIVER_ID,userId)
            .addSnapshotListener(eventListener)
    }


}