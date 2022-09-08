package com.example.chatapplication.data.response

import java.io.Serializable

data class UserResponse(
    val name: String = "", val email: String, val image: String = "", val token: String, val id: String = email, val status: Int = 0
) : Serializable
