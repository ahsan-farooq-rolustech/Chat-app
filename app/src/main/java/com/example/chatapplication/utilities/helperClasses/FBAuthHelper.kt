package com.example.chatapplication.utilities.helperClasses

import android.content.Context
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.utilities.utils.FBConstants
import com.example.chatapplication.utilities.utils.IFBAuthListener

class FBAuthHelper(private val context: Context)
{

    lateinit var mListener: IFBAuthListener

    fun setListener(listener: IFBAuthListener)
    {
        mListener = listener
    }

    fun authenticateUser(email: String, password: String, authType: Int)
    {
        when (authType)
        {
            FBConstants.LOGIN ->
            {
                ChatApplication.fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        mListener.onLoginSuccess()
                    }
                    else
                    {
                        mListener.onLoginError(task.exception.toString())
                    }

                }
            }
            FBConstants.REGISTER ->
            {
                ChatApplication.fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        mListener.onCompleteRegistration()
                    }
                    else
                    {
                        mListener.onRegistrationError(task.exception.toString())
                    }
                }
            }
        }
    }
}