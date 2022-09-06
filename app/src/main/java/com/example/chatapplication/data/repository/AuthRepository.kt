package com.example.chatapplication.data.repository

import com.example.chatapplication.ChatApplication
import com.example.chatapplication.utilities.utils.FBConstants
import com.example.chatapplication.utilities.utils.IFBAuthListener

class AuthRepository(private val mListinner:IFBAuthListener)
{
    fun checkIfUserExists(email:String)
    {
        val fireStore=ChatApplication.firestore
        val docRef = fireStore.collection(FBConstants.TABLE_USERS).document(ChatApplication.fbAuth.currentUser!!.uid)
        /*docRef.get().addOnSuccessListener{
        }*/ //for getting the simple values of table
        //if you want to get the realTimeChange
        docRef.addSnapshotListener { value, error ->
            if(value?.exists() == true)
            {
                mListinner.onUserExists()
            }
            else
            {
                mListinner.onUserDoesNotExists()
            }
        }
    }
}