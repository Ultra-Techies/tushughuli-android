package com.todoist_android.data.responses

data class SignupResponse(
    val created: Boolean,
    val id: Int,
    val username_valid: Boolean
)