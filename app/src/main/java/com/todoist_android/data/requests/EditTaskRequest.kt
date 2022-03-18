package com.todoist_android.data.requests

data class EditTaskRequest(
//    val id: Int,
    val description: String?,
    var dueDate: String,
    val reminder: String,
    var createdTime:String,
    val status: String,
    val title: String
)

