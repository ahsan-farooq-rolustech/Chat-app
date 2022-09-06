package com.example.chatapplication.utilities.utils

object FBConstants
{
    const val COL_EMAIL="email"
    const val TABLE_USERS="users"
    const val LOGIN = 1
    const val REGISTER = 2

}

object AppAlerts
{
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