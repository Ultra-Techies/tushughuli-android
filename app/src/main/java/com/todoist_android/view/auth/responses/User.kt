package com.todoist_android.view.auth.responses

data class User(
    val display_name: String,
    val email: String,
    val profile_photo: String,
    val username: String
)