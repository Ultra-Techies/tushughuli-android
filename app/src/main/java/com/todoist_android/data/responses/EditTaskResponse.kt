package com.todoist_android.data.responses

data class EditTaskResponse(
    val description: String,
    val dueDate: String,
    val id: Int,
    val reminder: String,
    val status: String,
    val title: String
)