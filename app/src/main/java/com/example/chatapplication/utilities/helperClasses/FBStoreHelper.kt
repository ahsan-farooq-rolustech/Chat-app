package com.example.chatapplication.utilities.helperClasses

import com.example.chatapplication.ChatApplication
import com.example.chatapplication.data.model.User
import com.example.chatapplication.utilities.utils.FBConstants
import com.example.chatapplication.utilities.utils.IFirestoreListinner

class FBStoreHelper
{

    private lateinit var mListener: IFirestoreListinner

    fun setListener(listener: IFirestoreListinner)
    {
        mListener = listener
    }

    fun insertUser(email: String,firstName:String,lastName:String,imageUri:String)
    {
        val docRef = ChatApplication.firestore.collection(FBConstants.COLLECTION_USERS).document(email)
        val userVal = HashMap<String, Any>()
        userVal[FBConstants.KEY_EMAIL] = email
        userVal[FBConstants.KEY_FIRST_NAME]=firstName
        userVal[FBConstants.KEY_LAST_NAME]=lastName
        userVal[FBConstants.KEY_USER_IMAGE]=imageUri

        //Successfully Inserted Listener, Failure listener can also be handled
        docRef.set(userVal).addOnSuccessListener {
            mListener.onUserInsertedSuccessfully()
        }

    }

    fun isUserExists(email: String)
    {
        ChatApplication.firestore.collection(FBConstants.COLLECTION_USERS).whereEqualTo(FBConstants.KEY_EMAIL, email).get().addOnCompleteListener { task ->
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
        ChatApplication.firestore.collection(FBConstants.COLLECTION_USERS).document(ChatApplication.fbAuth.currentUser?.email.toString()).update(FBConstants.KEY_FCM_TOKEN, token)
    }

    fun getUsers()
    {
        val userEmail=ChatApplication.fbAuth.currentUser?.email
        ChatApplication.firestore.collection(FBConstants.COLLECTION_USERS).get().addOnCompleteListener { task ->
            if(task.isSuccessful&& task.result !=null)
            {
                val users=ArrayList<User>()
                for(documentSnapShot in task.result)
                {
                    if(documentSnapShot.id==userEmail)
                    {
                        continue
                    }

                    val user=User(email = documentSnapShot.getString(FBConstants.KEY_EMAIL)?:"", token = documentSnapShot.getString(FBConstants.KEY_FCM_TOKEN)?:"")
                    users.add(user)
                }
                if(users.size>0)
                {
                    mListener.onUserGetSuccessfully(users)
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


//    fun getUserProfile() {
//        val docRef = FirebaseApp.fbStore.collection(FBConstants.TABLE_USERS).document(FirebaseApp.fbAuth.currentUser!!.uid)
//        val user = User()
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