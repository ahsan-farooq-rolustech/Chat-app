package com.example.chatapplication.utilities.helperClasses

import com.example.chatapplication.ChatApplication
import com.example.chatapplication.data.response.UserResponse
import com.example.chatapplication.utilities.utils.FBConstants
import com.example.chatapplication.utilities.utils.IFirestoreListener
import com.google.firebase.firestore.EventListener

class FBStoreHelper
{

    private lateinit var mListener: IFirestoreListener

    fun setListener(listener: IFirestoreListener)
    {
        mListener = listener
    }

    fun insertUser(email: String, firstName: String, lastName: String, imageUri: String)
    {
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

    fun setStatus(status: Int)
    {
        val docRef = ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_USERS).document(ChatApplication.fbAuth.currentUser!!.email!!)
        val userVal = HashMap<String, Any>()
        userVal[FBConstants.KEY_STATUS] = status

        //Successfully Inserted Listener, Failure listener can also be handled
        docRef.update(userVal).addOnSuccessListener {
            mListener.onStatusChangedSuccess()
        }
    }

    fun isUserExists(email: String)
    {
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_USERS).whereEqualTo(FBConstants.KEY_EMAIL, email).get().addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                if (task.result.size() > 0)
                {
                    mListener.onUserExists()
                }
                else
                {
                    mListener.onUserDoesNotExists()
                }
            }
            else
            {
                mListener.onFirestoreError(task.exception.toString())
            }
        }
    }

    fun updateToken(token: String)
    {
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_USERS).document(ChatApplication.fbAuth.currentUser?.email.toString()).update(FBConstants.KEY_FCM_TOKEN, token)
    }

    fun getUsers()
    {
        val userEmail = ChatApplication.fbAuth.currentUser?.email
//
//        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_USERS).addSnapshotListener(EventListener { value, error ->
//            if (error != null)
//            {
//                return@EventListener
//            }
//
//            if (value != null)
//            {
//                val userResponses=ArrayList<UserResponse>()
//                for(documentChanges in value.documents)
//                {
//                    if(documentChanges.id==userEmail)
//                    {
//                        continue
//                    }
//
//                    val userResponse=UserResponse(
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
            if (task.isSuccessful && task.result != null)
            {
                val userResponses = ArrayList<UserResponse>()
                for (documentSnapShot in task.result)
                {
                    if (documentSnapShot.id == userEmail)
                    {
                        continue
                    }

                    val userResponse = UserResponse(
                        email = documentSnapShot.getString(FBConstants.KEY_EMAIL) ?: "", token = documentSnapShot.getString(FBConstants.KEY_FCM_TOKEN) ?: "", name = documentSnapShot.getString(FBConstants.KEY_FIRST_NAME) ?: "", image = if (documentSnapShot.getString(FBConstants.KEY_USER_IMAGE) != null) documentSnapShot.getString(FBConstants.KEY_USER_IMAGE)!! else "", status = if (documentSnapShot.get(FBConstants.KEY_STATUS) != null) documentSnapShot.get(FBConstants.KEY_STATUS)!!.toString().toInt() else 0
                    )
                    userResponses.add(userResponse)
                }
                if (userResponses.size > 0)
                {
                    mListener.onUserGetSuccessfully(userResponses)
                }
                else
                {
                    mListener.onUserEmpty()
                }
            }
            else
            {
                mListener.onUserGetFailure(task.exception.toString())
            }
        }
    }

    fun getUserChanges()
    {
        val userEmail = ChatApplication.fbAuth.currentUser?.email
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_USERS).addSnapshotListener(EventListener { value, error ->
            if (error != null)
            {
//                mListener.onUserGetFailure(error.toString())
                return@EventListener
            }

            if (value != null)
            {
                val userResponses = ArrayList<UserResponse>()
                for (documentChanges in value.documents)
                {
                    if (documentChanges.id == userEmail)
                    {
                        continue
                    }

                    val userResponse = UserResponse(
                        email = documentChanges.getString(FBConstants.KEY_EMAIL) ?: "", token = documentChanges.getString(FBConstants.KEY_FCM_TOKEN) ?: "", name = documentChanges.getString(FBConstants.KEY_FIRST_NAME) ?: "", image = if (documentChanges.getString(FBConstants.KEY_USER_IMAGE) != null) documentChanges.getString(FBConstants.KEY_USER_IMAGE)!! else "", status = if (documentChanges.get(FBConstants.KEY_STATUS) != null) documentChanges.get(FBConstants.KEY_STATUS)!!.toString().toInt() else 0
                    )
                    userResponses.add(userResponse)
                }
                if (userResponses.size > 0)
                {
                    mListener.onGetUserChangesSuccessful(userResponses)
                }
//                else
//                {
//                    mListener.onUserEmpty()
//                }
            }
        })
    }

    fun uploadMessage(message: HashMap<String, Any>)
    {
        ChatApplication.firestore.collection(FBConstants.KEY_COLLECTION_CHAT).add(message)
    }


//    fun getUserProfile() {
//        val docRef = FirebaseApp.fbStore.collection(FBConstants.TABLE_USERS).document(FirebaseApp.fbAuth.currentUser!!.uid)
//        val user = UserResponse()
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