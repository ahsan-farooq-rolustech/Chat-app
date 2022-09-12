package com.example.chatapplication.data.responseModel

import java.io.Serializable

data class UserResponseModel(
    val name: String = "", val email: String="", val image: String = "", val token: String="", val id: String = email, val status: Int = 0
) : Serializable
