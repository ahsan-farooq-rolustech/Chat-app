package com.example.chatapplication.utilities.utils

import com.example.chatapplication.data.model.User

interface IFBAuthListener {
    fun onCompleteRegistration() {}
    fun onRegistrationError(error:String){}
    fun onLoginSuccess() {}
    fun onLoginError(error:String){}


}

interface IFirestoreListinner{
    fun onUserExists(){}
    fun onUserDoesNotExists(){}
    fun onFirestoreError(error:String){}
    fun onUserInsertedSuccessfully(){}
    fun onUserGetSuccessfully(users: ArrayList<User>) {}
    fun onUserEmpty(){}
    fun onUserGetFailure(error: String){}
}


