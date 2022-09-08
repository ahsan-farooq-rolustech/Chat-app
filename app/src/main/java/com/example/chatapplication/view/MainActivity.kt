package com.example.chatapplication.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapplication.R

class MainActivity : AppCompatActivity()
{

    companion object{
        lateinit var mActivity: Activity
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mActivity = this
    }
}