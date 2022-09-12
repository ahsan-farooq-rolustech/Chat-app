package com.example.chatapplication.view.authentication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.R
import com.example.chatapplication.view.MainActivity

class AuthActivity : AppCompatActivity()
{
    companion object
    {
        lateinit var mActivity:Activity
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {

        if(ChatApplication.fbAuth.currentUser?.email !=null)
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        mActivity=this
    }
}