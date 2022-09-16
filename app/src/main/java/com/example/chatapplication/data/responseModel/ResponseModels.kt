package com.example.chatapplication.data.responseModel

import java.io.Serializable
import java.util.*

data class ChatMessageResponseModel(var senderId: String = "", var receiverId: String = "", var message: String = "", var dateTime: String = "", var dateObj: Date? = null, var conversationId: String = "", var receiverImageUrl: String = "", var receiverName: String = "")
data class UserResponseModel(val name: String = "", val email: String = "", val image: String = "", val token: String = "", val id: String = email, val status: Int = 0) : Serializable
data class InboxResponseModel(val conversationId: String = "", val user1Email: String = "", val user2EMail: String = "", val user1ImageUrl: String = "", val user2ImageUrl: String = "", val user1Name: String = "", val user2Name: String = "", var lastMsg: String = "", var lastMsgTime: String = "",var lastMessageDateObj:Date?=null) : Serializable