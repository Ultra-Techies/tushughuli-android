package com.todoist_android.view


const val BASE_URL = "https://621ce943768a4e1020b93731.mockapi.io/api/v1/"

private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

fun validateEmail(email: String): Boolean {
    return email.matches(emailPattern.toRegex())
}

