package com.example.chatapplication

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatApplication : Application()
{
    companion object
    {
        lateinit var fbAuth: FirebaseAuth
        lateinit var firestore:FirebaseFirestore
    }

    override fun onCreate()
    {

        super.onCreate()
        initFirebase()
    }

    private fun initFirebase()
    {
        fbAuth = FirebaseAuth.getInstance()
        firestore=FirebaseFirestore.getInstance()
    }
}