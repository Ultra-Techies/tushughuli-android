package com.todoist_android.data.responses

data class LoginResponse(
    val id: Int,
    val username: String,
    val name: String,
    val email:String,
    val photo: String,
)