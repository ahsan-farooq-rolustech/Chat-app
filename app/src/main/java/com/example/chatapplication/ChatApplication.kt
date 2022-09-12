package com.example.chatapplication

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ChatApplication : Application(),LifecycleEventObserver
{
    companion object
    {
        lateinit var fbAuth: FirebaseAuth
        lateinit var firestore:FirebaseFirestore
        lateinit var firebaseStorage:FirebaseStorage
    }

    override fun onCreate()
    {

        super.onCreate()
        initFirebase()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun initFirebase()
    {
        fbAuth = FirebaseAuth.getInstance()
        firestore=FirebaseFirestore.getInstance()
        firebaseStorage=FirebaseStorage.getInstance()
    }

    override fun onTerminate()
    {
        super.onTerminate()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event)
    {
        when(event.name)
        {
            Lifecycle.Event.ON_START.name->
            {
                //foreground
                Log.d("ChatApplication","on start")
            }
            Lifecycle.Event.ON_STOP.name->
            {
                //background
                Log.d("ChatApplication","on stop")
            }
            Lifecycle.Event.ON_DESTROY.name->
            {
                //app is killed
                Log.d("ChatApplication","on destroy")
            }
        }
    }


}