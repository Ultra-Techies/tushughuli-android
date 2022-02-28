package com.todoist_android.view

private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

fun validateEmail(email: String): Boolean {
    return email.matches(emailPattern.toRegex())
}

