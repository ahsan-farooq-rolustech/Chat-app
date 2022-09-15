package com.example.chatapplication.utilities.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.utils.AppConstants


class ExitService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ExitService", "exit service called")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() { //Log.d("ExitService","app started")
        super.onCreate()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val helper = FBStoreHelper()
        helper.setStatus(AppConstants.STATUS_OFFLINE)
    }

    override fun onDestroy() {
        Log.d("ExitService", "app killed")
        super.onDestroy()
    }
}