package com.example.chatapplication.data.model

import java.io.Serializable

data class User(
    val name:String="",
    val email:String,
    val image:String="",
    val token:String
):Serializable
