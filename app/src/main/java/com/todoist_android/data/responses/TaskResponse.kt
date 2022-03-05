package com.todoist_android.data.responses

data class TaskResponse(
    val description: String,
    val due_date: String,
    val id: Int,
    val status: String,
    val title: String
)