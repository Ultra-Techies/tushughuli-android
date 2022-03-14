package com.todoist_android.data.requests

data class SignUpRequest(
    val email: String,
    val name: String,
    val password: String,
    val photo: String,
    val username: String
)