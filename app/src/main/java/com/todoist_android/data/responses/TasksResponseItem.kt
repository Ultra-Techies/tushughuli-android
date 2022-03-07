package com.todoist_android.data.responses

data class TasksResponseItem(
    val description: String,
    val due_date: String,
    val id: String,
    val status: String,
    val title: String
)