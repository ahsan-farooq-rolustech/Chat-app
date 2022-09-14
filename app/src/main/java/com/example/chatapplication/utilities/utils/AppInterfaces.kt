package com.example.chatapplication.utilities.utils

import com.example.chatapplication.data.responseModel.ChatMessageResponseModel
import com.example.chatapplication.data.responseModel.InboxResponseModel
import com.example.chatapplication.data.responseModel.UserResponseModel

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
    fun onUserGetSuccessfully(userResponsModels: ArrayList<UserResponseModel>) {}
    fun onUserEmpty(){}
    fun onUserGetFailure(error: String){}
    fun onStatusChangedSuccess(){}
    fun onGetUserChangesSuccessful(userResponseModels: ArrayList<UserResponseModel>){}
    fun onChatCreatedSuccess(){}
    fun onConversationGetSuccess(conversationId:String){}
    fun onGetMessagesSuccessful(list:ArrayList<ChatMessageResponseModel>){}
    fun onUserImageUploadedSuccessfully(imageUrl:String){}
    fun onUserImageUploadProgress(progress: Int){}
    fun onGetUserInboxSuccessful(list:ArrayList<InboxResponseModel>){}
    fun onGetUserInboxFailure(error:String){}
}


interface IUserListener
{
    fun onUserClicked(userResponseModel:UserResponseModel){}
}

