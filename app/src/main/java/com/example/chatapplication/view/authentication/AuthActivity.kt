package com.example.chatapplication.view.authentication

import android.content.Intent
import android.os.Bundle
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.R
import com.example.chatapplication.view.MainActivity
import com.example.chatapplication.view.base.ActivityBase

class AuthActivity : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {

        if (ChatApplication.fbAuth.currentUser?.email != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}