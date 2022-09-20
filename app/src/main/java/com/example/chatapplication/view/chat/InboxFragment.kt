package com.example.chatapplication.view.chat

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.R
import com.example.chatapplication.data.responseModel.DialogItems
import com.example.chatapplication.data.responseModel.InboxResponseModel
import com.example.chatapplication.data.responseModel.UserResponseModel
import com.example.chatapplication.databinding.FragmentInboxBinding
import com.example.chatapplication.utilities.bottomsheets.BottomSheetDeleteConversation
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.utils.*
import com.example.chatapplication.view.authentication.AuthActivity
import com.example.chatapplication.view.base.ActivityBase
import com.example.chatapplication.view.chat.adapter.InboxAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class InboxFragment : Fragment(), IFBAuthListener, IFirestoreListener, View.OnClickListener, IInboxListener, IBottomSheetListeners, IDialogListeners {

    private lateinit var binding: FragmentInboxBinding
    private lateinit var fsHelper: FBStoreHelper
    private var mList = ArrayList<InboxResponseModel>()
    private lateinit var adapter: InboxAdapter
    private lateinit var conversationIdToBeDeleted: String


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
        setAdapter() //deleteChatsWithHi()
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvInbox)
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

    private val swipeCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            var position = viewHolder.adapterPosition
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    deleteChat(mList[position].conversationId)
                }
                ItemTouchHelper.RIGHT -> {

                }

            }

        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive).addSwipeLeftBackgroundColor(ContextCompat.getColor(ActivityBase.activity, R.color.error)).addSwipeLeftActionIcon(R.drawable.ic_delete).addSwipeRightBackgroundColor(ContextCompat.getColor(ActivityBase.activity, R.color.green)).addSwipeRightActionIcon(R.drawable.ic_archive).create().decorate()

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    private fun deleteChat(conversationId: String) {
        conversationIdToBeDeleted = conversationId
        showDeleteConformationDialog()
    }

    private fun showDeleteConformationDialog() {
        val bottomSheetDeleteConformation = BottomSheetDeleteConversation(ActivityBase.activity)
        if (!bottomSheetDeleteConformation.isAdded) {
            bottomSheetDeleteConformation.show(ActivityBase.activity.supportFragmentManager, bottomSheetDeleteConformation.tag)
        }
        bottomSheetDeleteConformation.setListener(this)
    }

    override fun onClickDeleteChat() {
        ActivityBase.activity.showConfirmationDialog(DialogItems(title = "", description = "Are you sure you want to delete this chat?", buttonPositive = "Yes", buttonNegative = "No"), this)
    }

    override fun onClickPositiveButton() {
        if (this::conversationIdToBeDeleted.isInitialized) {
            binding.isLoading = true
            fsHelper.deleteConversation(conversationIdToBeDeleted)
        }
    }

    override fun onConversationDeletedSuccess() {
        ActivityBase.activity.showToastMessage(AppAlerts.CONVERSATION_DELETE_SUCCESS)
        fsHelper.getConversations(ChatApplication.fbAuth.currentUser?.email!!)

    }


    //    private fun deleteChatsCollection()
    //    {
    //        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CONVERSATIONS).document().collection(FBConstants.KEY_COLLECTION_CHAT).document().delete().addOnSuccessListener {
    //            Log.d("deleteChatsCollection","deleteSuccess")
    //        }.addOnFailureListener {
    //            Log.d("deleteChatsCollection","$it")
    //        }
    //    }
    //
    //    private fun deleteChatsWithHi()
    //    {
    //        val chatCollection=ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CONVERSATIONS).document("eBLYMsr0sbGTL84iz9fB").collection(FBConstants.KEY_COLLECTION_CHAT)
    //        chatCollection.whereEqualTo(FBConstants.KEY_MESSAGE,"hi").get().addOnCompleteListener { task->
    //            if(task.isSuccessful)
    //            {
    //                for(documentSnapShot in task.result)
    //                {
    //                    Log.d("deleteChatsWithHi",documentSnapShot.id)
    //                    chatCollection.document(documentSnapShot.id).delete()
    //                }
    //                Log.d("deleteChatsWithHi","empty result")
    //            }
    //            else
    //            {
    //                Log.d("deleteChatsWithHi",task.exception.toString())
    //            }
    //        }
    //    }

}