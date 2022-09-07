package com.example.chatapplication.utilities.utils

object FBConstants
{
    const val KEY_EMAIL="email"
    const val KEY_FIRST_NAME="first_name"
    const val KEY_LAST_NAME="last_name"
    const val KEY_USER_IMAGE="image"
    const val COLLECTION_USERS="users"
    const val KEY_FCM_TOKEN="fcmToken"
    const val LOGIN = 1
    const val REGISTER = 2

}

object AppAlerts
{
    const val NO_USER_AVALABLE="No users available"
    const val NO_USER_EXCEPTION = "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted."

    const val INCORRECT_EMAIL="Incorrect Email"
    const val PASSWORD_LENGTH_SHORT="Length must be greater than 5"
    const val PASSWORD_DONOT_MATCH="Donot match"
}

object ValidationConstants
{
    const val PASSWORD_DONOT_MATCH=-1
    const val PASSWORD_LEN_ERROR=-2
    const val PASSWORK_OKAY=0
}

object AppConstants
{
    const val USER_EXISTANCE_UNKNOWN=99
    const val USER_EXISTS=999
    const val USER_DOESNOT_EXISTS=9999

}