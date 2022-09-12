package com.example.chatapplication.data.responseModel

import java.io.Serializable
import java.util.*

data class ChatMessageResponseModel(var senderId: String = "", var receiverId: String = "", var message: String = "", var dateTime: String = "", var dateObj: Date? = null, var conversationId: String = "", var conversationNumber: String = "", var conversationImage: String = "", var conversationName: String = "")
data class UserResponseModel(val name: String = "", val email: String = "", val image: String = "", val token: String = "", val id: String = email, val status: Int = 0) : Serializable
