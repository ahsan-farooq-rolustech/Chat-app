package com.example.chatapplication.utilities.utils

object Validators {
    fun validateEmail(email: String): Boolean {
        if (email.isEmpty()) {
            return false
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String, confirmPassword: String = ""): Int {
        if (confirmPassword.isNotEmpty()) {
            if (password != confirmPassword) {
                return ValidationConstants.PASSWORD_NOT_MATCH
            }
        }

        if (password.length < 6) {
            return ValidationConstants.PASSWORD_LEN_ERROR
        }

        return ValidationConstants.PASSWORD_OKAY
    }

}