package com.todoist_android.data.responses

data class SignupResponse(
    val email: String,
    val id: Int,
    val name: String,
    val photo: String,
    val username: String
)