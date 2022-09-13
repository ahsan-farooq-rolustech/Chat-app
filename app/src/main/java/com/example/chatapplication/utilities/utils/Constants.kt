package com.example.chatapplication.utilities.utils

object FBConstants {
    const val KEY_EMAIL = "email"
    const val KEY_FIRST_NAME = "first_name"
    const val KEY_LAST_NAME = "last_name"
    const val KEY_USER_IMAGE = "image"
    const val KEY_COLLECTION_USERS = "users"
    const val KEY_FCM_TOKEN = "fcmToken"
    const val LOGIN = 1
    const val REGISTER = 2
    const val KEY_COLLECTION_CHAT = "chat"
    const val KEY_SENDER_ID = "senderId"
    const val KEY_RECEIVER_ID = "receiverId"
    const val KEY_MESSAGE = "message"
    const val KEY_TIMESTAMP = "timestamp"
    const val KEY_STATUS = "status"
    const val KEY_COLLECTION_CONVERSATIONS = "conversations"
    const val KEY_USER1 = "user1"
    const val KEY_USER2 = "user2"
    const val KEY_STORAGE_USER_IMAGES = "user_images/"
    const val KEY_SENDER_NAME = "senderName"
    const val KEY_RECEIVER_NAME = "receiverName"
    const val KEY_SENDER_IMAGE = "senderImage"
    const val KEY_RECEIVER_IMAGE = "receiverImage"
    const val KEY_LAST_MESSAGE = "lastMessage"
}

object AppAlerts {
    const val EMPTY_MESSAGE = "Empty message"
    const val NO_USER_AVALABLE = "No users available"
    const val INCORRECT_EMAIL = "Incorrect Email"
    const val PASSWORD_LENGTH_SHORT = "Length must be greater than 5"
    const val PASSWORD_DONOT_MATCH = "Donot match"
    const val FIRST_NAME_ERROR = "First name cannot be empty"
    const val LAST_NAME_ERROR = "Last name cannot be empty"
    const val INSERT_USER_IMAGE="Please Insert User Image"
    const val REGISTRATION_ERROR="Registration falied due to some error"
    const val LOGIN_SUCCESS="Login Successful"
    const val REGISTRATION_SUCCESS="Registration is successful"
    const val USER_INSRTED_FIRESTORE="User successfully in Firestore"
}

object ValidationConstants {
    const val PASSWORD_DONOT_MATCH = -1
    const val PASSWORD_LEN_ERROR = -2
    const val PASSWORK_OKAY = 0
}

object AppConstants {
    const val MODE_INITIAL_STATE = 99
    const val MODE_SIGN_IN = 999
    const val MODE_SIGN_UP = 9999
    const val STATUS_ACTIVE = 1
    const val STATUS_IN_ACTIVE = 2
    const val STATUS_OFFLINE = 3
    const val VIEW_TYPE_SENT = 1
    const val VIEW_TYPE_RECEIVE = 2
}