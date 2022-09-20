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
    const val KEY_USER_1_NAME = "user1_name"
    const val KEY_USER_2_NAME = "user2_name"
    const val KEY_USER_1_IMAGE = "user1_image_url"
    const val KEY_USER_2_IMAGE = "user2_image_url"
    const val KEY_LAST_MESSAGE = "lastMessage"

    const val KEY_COLLECTION_USER_ACTIVITY = "user_activities"
    const val KEY_IS_USER_TYPING = "is_user_typing"

}

object AppAlerts {
    const val CONVERSATION_DELETE_SUCCESS="Conversation Deleted Successfully."
    const val GET_INBOX_FAILURE = "Error in getting chats"
    const val EMPTY_MESSAGE = "Empty message"
    const val NO_USER_AVAILABLE = "No users available"
    const val INCORRECT_EMAIL = "Incorrect Email"
    const val PASSWORD_LENGTH_SHORT = "Length must be greater than 5"
    const val PASSWORD_NOT_MATCH = "Don't match"
    const val FIRST_NAME_ERROR = "First name cannot be empty"
    const val LAST_NAME_ERROR = "Last name cannot be empty"
    const val INSERT_USER_IMAGE = "Please Insert User Image"
    const val REGISTRATION_ERROR = "Registration failed due to some error"
    const val LOGIN_SUCCESS = "Login Successful"
    const val REGISTRATION_SUCCESS = "Registration is successful"
    const val USER_INSERTED_SUCCESS = "user successfully in Firestore"
}

object ValidationConstants {
    const val PASSWORD_NOT_MATCH = -1
    const val PASSWORD_LEN_ERROR = -2
    const val PASSWORD_OKAY = 0
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

object UserPrefConstants {
    const val IMAGE_URL = "image_url"
    const val FULL_NAME = "full_name"
    const val EMAIL = "email"
}