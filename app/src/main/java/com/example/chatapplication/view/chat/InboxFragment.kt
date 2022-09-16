package com.example.chatapplication.view.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.R
import com.example.chatapplication.data.responseModel.InboxResponseModel
import com.example.chatapplication.databinding.FragmentInboxBinding
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.utils.*
import com.example.chatapplication.view.authentication.AuthActivity
import com.example.chatapplication.view.base.ActivityBase
import com.example.chatapplication.view.chat.adapter.InboxAdapter


class InboxFragment : Fragment(), IFBAuthListener, IFirestoreListener, View.OnClickListener, IInboxListener {

    private lateinit var binding: FragmentInboxBinding
    private lateinit var fsHelper: FBStoreHelper
    private var mList = ArrayList<InboxResponseModel>()
    private lateinit var adapter: InboxAdapter


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
        deleteChatsWithHi()
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
            adapter.notifyItemRangeChanged(0, mList.size - 1)
        } else {
            binding.isInboxEmpty = true
        }
    }

    override fun onGetUserInboxFailure(error: String) {
        ActivityBase.activity.showToastMessage(AppAlerts.GET_INBOX_FAILURE)
    }

    override fun onClickConversation(position: Int) {

    }

    private fun deleteChatsCollection()
    {
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CONVERSATIONS).document().collection(FBConstants.KEY_COLLECTION_CHAT).document().delete().addOnSuccessListener {
            Log.d("deleteChatsCollection","deleteSuccess")
        }.addOnFailureListener {
            Log.d("deleteChatsCollection","$it")
        }
    }

    private fun deleteChatsWithHi()
    {
        val chatCollection=ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CONVERSATIONS).document("eBLYMsr0sbGTL84iz9fB").collection(FBConstants.KEY_COLLECTION_CHAT)
        chatCollection.whereEqualTo(FBConstants.KEY_MESSAGE,"hi").get().addOnCompleteListener { task->
            if(task.isSuccessful)
            {
                for(documentSnapShot in task.result)
                {
                    Log.d("deleteChatsWithHi",documentSnapShot.id)
                    chatCollection.document(documentSnapShot.id).delete()
                }
                Log.d("deleteChatsWithHi","empty result")
            }
            else
            {
                Log.d("deleteChatsWithHi",task.exception.toString())
            }
        }
    }

}