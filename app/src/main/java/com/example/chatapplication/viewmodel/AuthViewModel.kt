package com.example.chatapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.data.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository,application: Application):AndroidViewModel(application)
{
    fun checkIfUserExists(email:String)
    {
        authRepository.checkIfUserExists(email)
    }
}