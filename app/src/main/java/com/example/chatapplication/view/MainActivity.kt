package com.example.chatapplication.view

import android.content.Intent
import android.os.Bundle
import com.example.chatapplication.R
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.services.ExitService
import com.example.chatapplication.utilities.utils.AppConstants
import com.example.chatapplication.utilities.utils.IFirestoreListener
import com.example.chatapplication.view.base.ActivityBase

class MainActivity : ActivityBase(), IFirestoreListener {

    private lateinit var fsHelper: FBStoreHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, ExitService::class.java))
        setHelper()
    }

    private fun setHelper() {
        fsHelper = FBStoreHelper()
        fsHelper.setListener(this)
    }

    override fun onResume() {
        fsHelper.setStatus(AppConstants.STATUS_ACTIVE)
        super.onResume()
    }

    override fun onPause() {
        fsHelper.setStatus(AppConstants.STATUS_IN_ACTIVE)
        super.onPause()
    }

}