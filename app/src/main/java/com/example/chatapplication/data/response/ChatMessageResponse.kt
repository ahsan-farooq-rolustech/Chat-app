package com.example.chatapplication.data.response

import java.util.*

data class ChatMessageResponse(
    val senderId:String,
    val receiverId:String,
    val message:String,
    val dateTime:String,
    val dateObj:Date?,
    val conversationId:String="",
    val conversationNumber:String="",
    val conversationImage:String=""
)
