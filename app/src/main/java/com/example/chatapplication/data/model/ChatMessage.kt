package com.example.chatapplication.data.model

data class ChatMessage(
    val senderId:String,
    val receiverId:String,
    val message:String,
    val dateTime:String
)
