package com.example.chatapplication.utilities.helperClasses

import com.example.chatapplication.ChatApplication
import com.example.chatapplication.data.responseModel.ChatMessageResponseModel
import com.example.chatapplication.data.responseModel.UserResponseModel
import com.example.chatapplication.utilities.utils.FBConstants
import com.example.chatapplication.utilities.utils.IFirestoreListener
import com.example.chatapplication.utilities.utils.getReadableFormat
import com.google.firebase.firestore.EventListener

class FBStoreHelper {

    private lateinit var mListener: IFirestoreListener

    fun setListener(listener: IFirestoreListener) {
        mListener = listener
    }

    fun insertUser(email: String, firstName: String, lastName: String, imageUri: String) {
        val docRef = ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_USERS).document(email)
        val userVal = HashMap<String, Any>()
        userVal[FBConstants.KEY_EMAIL] = email
        userVal[FBConstants.KEY_FIRST_NAME] = firstName
        userVal[FBConstants.KEY_LAST_NAME] = lastName
        userVal[FBConstants.KEY_USER_IMAGE] = imageUri

        //Successfully Inserted Listener, Failure listener can also be handled
        docRef.set(userVal).addOnSuccessListener {
            mListener.onUserInsertedSuccessfully()
        }

    }

    fun setStatus(status: Int) {
        val email = ChatApplication.fbAuth.currentUser?.email
        if (email != null) {
            val docRef = ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_USERS).document()
            val userVal = HashMap<String, Any>()
            userVal[FBConstants.KEY_STATUS] = status

            //Successfully Inserted Listener, Failure listener can also be handled
            docRef.update(userVal).addOnSuccessListener {
                mListener.onStatusChangedSuccess()
            }
        }

    }

    fun isUserExists(email: String) {
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_USERS).whereEqualTo(FBConstants.KEY_EMAIL, email).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.size() > 0) {
                    mListener.onUserExists()
                } else {
                    mListener.onUserDoesNotExists()
                }
            } else {
                mListener.onFirestoreError(task.exception.toString())
            }
        }
    }

    fun updateToken(token: String) {
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_USERS).document(ChatApplication.fbAuth.currentUser?.email.toString()).update(FBConstants.KEY_FCM_TOKEN, token)
    }

    fun getUsers() {
        val userEmail = ChatApplication.fbAuth.currentUser?.email // //        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_USERS).addSnapshotListener(EventListener { value, error -> //            if (error != null) //            { //                return@EventListener //            } //
        //            if (value != null)
        //            {
        //                val userResponses=ArrayList<UserResponseModel>()
        //                for(documentChanges in value.documents)
        //                {
        //                    if(documentChanges.id==userEmail)
        //                    {
        //                        continue
        //                    }
        //
        //                    val userResponse=UserResponseModel(
        //                        email = documentChanges.getString(FBConstants.KEY_EMAIL)?:"",
        //                        token = documentChanges.getString(FBConstants.KEY_FCM_TOKEN)?:"",
        //                        name = documentChanges.getString(FBConstants.KEY_FIRST_NAME)?:"",
        //                        image = if (documentChanges.getString(FBConstants.KEY_USER_IMAGE)!=null)documentChanges.getString(FBConstants.KEY_USER_IMAGE)!! else "",
        //                        status = if ( documentChanges.get(FBConstants.KEY_STATUS)!=null)  documentChanges.get(FBConstants.KEY_STATUS)!!.toString().toInt() else 0
        //                    )
        //                    userResponses.add(userResponse)
        //                }
        //                if(userResponses.size>0)
        //                {
        //                    mListener.onUserGetSuccessfully(userResponses)
        //                }
        //                else
        //                {
        //                    mListener.onUserEmpty()
        //                }
        //            }
        //        })


        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_USERS).get().addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                val userResponsModels = ArrayList<UserResponseModel>()
                for (documentSnapShot in task.result) {
                    if (documentSnapShot.id == userEmail) {
                        continue
                    }

                    val userResponseModel = UserResponseModel(email = documentSnapShot.getString(FBConstants.KEY_EMAIL) ?: "", token = documentSnapShot.getString(FBConstants.KEY_FCM_TOKEN) ?: "", name = documentSnapShot.getString(FBConstants.KEY_FIRST_NAME) ?: "", image = if (documentSnapShot.getString(FBConstants.KEY_USER_IMAGE) != null) documentSnapShot.getString(FBConstants.KEY_USER_IMAGE)!! else "", status = if (documentSnapShot.get(FBConstants.KEY_STATUS) != null) documentSnapShot.get(FBConstants.KEY_STATUS)!!.toString().toInt() else 0)
                    userResponsModels.add(userResponseModel)
                }
                if (userResponsModels.size > 0) {
                    mListener.onUserGetSuccessfully(userResponsModels)
                } else {
                    mListener.onUserEmpty()
                }
            } else {
                mListener.onUserGetFailure(task.exception.toString())
            }
        }
    }

    fun getUserChanges() {
        val userEmail = ChatApplication.fbAuth.currentUser?.email
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_USERS).addSnapshotListener(EventListener { value, error ->
            if (error != null) { //                mListener.onUserGetFailure(error.toString())
                return@EventListener
            }

            if (value != null) {
                val userResponsModels = ArrayList<UserResponseModel>()
                for (documentChanges in value.documents) {
                    if (documentChanges.id == userEmail) {
                        continue
                    }

                    val userResponseModel = UserResponseModel(email = documentChanges.getString(FBConstants.KEY_EMAIL) ?: "", token = documentChanges.getString(FBConstants.KEY_FCM_TOKEN) ?: "", name = documentChanges.getString(FBConstants.KEY_FIRST_NAME) ?: "", image = if (documentChanges.getString(FBConstants.KEY_USER_IMAGE) != null) documentChanges.getString(FBConstants.KEY_USER_IMAGE)!! else "", status = if (documentChanges.get(FBConstants.KEY_STATUS) != null) documentChanges.get(FBConstants.KEY_STATUS)!!.toString().toInt() else 0)
                    userResponsModels.add(userResponseModel)
                }
                if (userResponsModels.size > 0) {
                    mListener.onGetUserChangesSuccessful(userResponsModels)
                } //                else
                //                {
                //                    mListener.onUserEmpty()
                //                }
            }
        })
    }

    fun uploadMessage(message: HashMap<String, Any>, conversationId: String) {
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CONVERSATIONS).document(conversationId).collection(FBConstants.KEY_COLLECTION_CHAT).add(message)
    }


    fun checkIfConversationExists(user1Id: String, user2Id: String) {
        val collectionReference = ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CONVERSATIONS)
        var isFirstQuerySuccess = false
        var isFirstQueryExecuted = false
        collectionReference.whereEqualTo(FBConstants.KEY_USER1, user1Id).whereEqualTo(FBConstants.KEY_USER2, user2Id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.size() > 0) { //means that there exists a conversation of these two chats
                    val conversationId = task.result.documents[0].id
                    mListener.onConversationGetSuccess(conversationId)
                    isFirstQuerySuccess = true
                    isFirstQueryExecuted = true

                    return@addOnCompleteListener
                } else { //there is no conversation created earlier so create a new one and get its id
                    isFirstQuerySuccess = false
                    isFirstQueryExecuted=true

                }
            } else {
                //the query is no executed as there are some errors
                isFirstQuerySuccess=false
                isFirstQueryExecuted=false

            }
        }
        if (!isFirstQuerySuccess)
            collectionReference.whereEqualTo(FBConstants.KEY_USER1, user2Id).whereEqualTo(FBConstants.KEY_USER2, user1Id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.size() > 0) { //means that there exists a conversation of these two chats
                    val conversationId = task.result.documents[0].id
                    mListener.onConversationGetSuccess(conversationId)
                    isFirstQuerySuccess = true
                    isFirstQueryExecuted = true
                } else {
                    //there is no conversation created of these two chats so create a new one
                    if (!isFirstQuerySuccess && isFirstQueryExecuted)
                        createNewConversation(user1Id, user2Id)

                }
            }
        }



    }

    private fun createNewConversation(user1Id: String, user2Id: String) {
        val data = HashMap<String, Any>()
        data[FBConstants.KEY_USER1] = user1Id
        data[FBConstants.KEY_USER2] = user2Id
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CONVERSATIONS).add(data).addOnSuccessListener { document ->
            val conversationId = document.id
            mListener.onConversationGetSuccess(conversationId)
        }
    }

    fun getMessages(conversationId: String) {
        val messagesList = ArrayList<ChatMessageResponseModel>()
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CONVERSATIONS).document(conversationId).collection(FBConstants.KEY_COLLECTION_CHAT).addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (value != null) {
                for (documentChange in value.documentChanges) {
                    val chatMessageResponseModel = ChatMessageResponseModel(senderId = documentChange.document.getString(FBConstants.KEY_SENDER_ID) ?: "", receiverId = documentChange.document.getString(FBConstants.KEY_RECEIVER_ID) ?: "", message = documentChange.document.getString(FBConstants.KEY_MESSAGE) ?: "", dateTime = documentChange.document.getDate(FBConstants.KEY_TIMESTAMP)?.getReadableFormat() ?: "", dateObj = documentChange.document.getDate(FBConstants.KEY_TIMESTAMP))
                    messagesList.add(chatMessageResponseModel)
                }
                messagesList.sortBy {
                    it.dateObj
                }
                mListener.onGetMessagesSuccessful(messagesList)
            }
        }
    }


    //    fun getUserProfile() {
    //        val docRef = FirebaseApp.fbStore.collection(FBConstants.TABLE_USERS).document(FirebaseApp.fbAuth.currentUser!!.uid)
    //        val user = UserResponseModel()
    //        user.id = FirebaseApp.fbAuth.currentUser!!.uid
    //
    //        /*docRef.get().addOnSuccessListener{
    //        }*/ //for getting the simple values of table
    //
    //        //if you want to get the realTimeChange
    //        docRef.addSnapshotListener { value, error ->
    //            user.firstName = value!!.get(FBConstants.COL_FIRST_NAME).toString()
    //            user.lastName = value!!.get(FBConstants.COL_LAST_NAME).toString()
    //            user.phoneNumber = value!!.get(FBConstants.COL_PHONE).toString()
    //            user.email = value!!.get(FBConstants.COL_EMAIL).toString()
    //            mListener.getUserProfile(user)
    //        }
    //    }
}