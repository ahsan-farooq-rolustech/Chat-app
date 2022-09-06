package com.example.chatapplication.viewmodel.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.data.repository.AuthRepository
import com.example.chatapplication.viewmodel.AuthViewModel
import java.lang.Appendable

class AuthViewModelFactory(private val application:Application,private val authRepository: AuthRepository):ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        return AuthViewModel(authRepository,application) as T
    }
}