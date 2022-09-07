package com.example.chatapplication.utilities.utils

interface IFBAuthListener {
    fun onCompleteRegistration() {}
    fun onRegistrationError(error:String){}
    fun onCompleteLogin() {}
    fun onLoginError(error:String){}
    fun onUserExists(){}
    fun onUserDoesNotExists(){}
    fun onFirestoreError(error:String){}
    fun onUserInsertedSuccessfully(){}

}

