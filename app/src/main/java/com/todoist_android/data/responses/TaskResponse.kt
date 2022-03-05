package com.todoist_android.data.responses

data class TaskResponse(
    val description: String,
    val due_date: String,
    val status: String,
    val id: Int,
    val title: String
)