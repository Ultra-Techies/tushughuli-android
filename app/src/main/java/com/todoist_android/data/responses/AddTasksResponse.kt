package com.todoist_android.data.responses

data class AddTasksResponse(
    val id: Int,
    val title: String,
    val description: String,
    val reminder: String,
    val dueDate: String,
    val status: String,

)