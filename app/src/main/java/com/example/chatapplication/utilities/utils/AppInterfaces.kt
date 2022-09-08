package com.example.chatapplication.utilities.utils

import com.example.chatapplication.data.response.UserResponse

interface IFBAuthListener
{
    fun onCompleteRegistration() {}
    fun onRegistrationError(error:String){}
    fun onLoginSuccess() {}
    fun onLoginError(error:String){}
}

interface IFirestoreListener
{
    fun onUserExists(){}
    fun onUserDoesNotExists(){}
    fun onFirestoreError(error:String){}
    fun onUserInsertedSuccessfully(){}
    fun onUserGetSuccessfully(userResponses: ArrayList<UserResponse>) {}
    fun onUserEmpty(){}
    fun onUserGetFailure(error: String){}
    fun onStatusChangedSuccess(){}
}


interface IUserListener
{
    fun onUserClicked(userResponse:UserResponse){}
}

