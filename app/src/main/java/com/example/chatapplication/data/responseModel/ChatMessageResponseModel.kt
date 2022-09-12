package com.example.chatapplication.data.responseModel

import java.util.*

data class ChatMessageResponseModel(
    var senderId:String="",
    var receiverId:String="",
    var message:String="",
    var dateTime:String="",
    var dateObj:Date?=null,
    var conversationId:String="",
    var conversationNumber:String="",
    var conversationImage:String="",
    var conversationName:String=""
)
