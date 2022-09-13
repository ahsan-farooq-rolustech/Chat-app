package com.example.chatapplication.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.chatapplication.R
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.services.ExitService
import com.example.chatapplication.utilities.utils.AppConstants
import com.example.chatapplication.utilities.utils.IFirestoreListener
import com.example.chatapplication.utilities.utils.showToastMessage
import com.example.chatapplication.view.base.ActivityBase

class MainActivity : ActivityBase(), IFirestoreListener {

    private lateinit var firestoreHelper: FBStoreHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, ExitService::class.java))
        setHelpers()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        Handler(Looper.myLooper()!!).postDelayed({

        }, 1000)
        this.showToastMessage("I am clicked")
    }

    private fun setHelpers() {
        firestoreHelper = FBStoreHelper()
        firestoreHelper.setListener(this)
    }

    override fun onResume() {
        firestoreHelper.setStatus(AppConstants.STATUS_ACTIVE)
        super.onResume()
    }

    override fun onPause() {
        firestoreHelper.setStatus(AppConstants.STATUS_IN_ACTIVE)
        super.onPause()
    }

}